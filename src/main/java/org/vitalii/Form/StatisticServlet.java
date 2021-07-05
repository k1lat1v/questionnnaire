package org.vitalii.Form;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;

@WebServlet(name = "statisticServlet", urlPatterns = {"/statistic"})
public class StatisticServlet extends HttpServlet {

    static final File answers = new File("statistic", "answers.txt");
    static final String TEMPLATE = "<html>" +
            "<head><title>Статистика ответов Prog Kiev</title>" +
            "<link rel=\"stylesheet\" href=\"css/statisticStyle.css\">" +
            "</head>" +
            "<body>" +
                "<div class=\"content\">%s</div>" +
            "</body>" +
            "</html>";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");

        HttpSession session = req.getSession(false);
        if(session == null || session.getAttribute("user_firstName") == null){
            String noSession = "<h1> Чтобы посмотреть статистику ответов, сначала пройдите анкету! </h1> <br>" +
                    "<a href=\"form.jsp\">Пройти анкету</a>";
            resp.getWriter().println(String.format(TEMPLATE, noSession));
            return;
        }

        String[] answersArray = readFromFile(answers);

        int[][] counter = getStatistic(answersArray);

        String table = createTable(counter);

        String retry = "<br> <a href=\"/submit?action=retry\">Пройти анкету еще раз</a>";

        resp.getWriter().println(String.format(TEMPLATE, (table + retry)));
    }

    private String[] readFromFile(File file){

        String[] answersArray = new String[0];

        try(FileInputStream is = new FileInputStream(file)){
            byte[] readArray = new byte[is.available()];
            is.read(readArray);
            answersArray = new String(readArray, StandardCharsets.UTF_8).split(System.lineSeparator());
        }catch (IOException e){
            e.printStackTrace();
        }

        return answersArray;
    }

    private int[][] getStatistic(String[] infoArray){

        int quantityOfQuestions = infoArray[infoArray.length-1].split(" ").length;
        int quantityOfAnswers = 2;

        int[][] counter = new int[quantityOfQuestions][quantityOfAnswers];

        for(int i=0; i<infoArray.length; i++){
            String[] temp = infoArray[i].split(" ");
            try{
                for(int j=0; j<temp.length; j++){
                    if(temp[j].equals("yes")){
                        counter[j][0]++;
                    }else{
                        counter[j][1]++;
                    }
                }
            }catch (ArrayIndexOutOfBoundsException e){
                // Сначала два вопроса, но если добавить третий, то чтобы статистика считала и первые два, и новый третий,
                // то нужно схватить исключение и пропустить дальше цикл
            }
        }
        return counter;
    }

    private String createTable(int[][] counter){
        StringJoiner html = new StringJoiner(System.lineSeparator());
        html.add("<table border=\"1\">")
                .add("<caption> <h1> Статистика ответов: </h1> </caption>")
                .add("<tr>")
                .add("<td></td>");

        for(int i=0; i<counter[0].length; i++){
            html.add("<th>Ответ " + (i+1) + "</th>");
        }

        html.add("</tr>");

        for(int i=0; i<counter.length; i++){
            html.add("<tr>")
                    .add("<th>Вопрос " + (i+1) + "</th>");
            for(int j=0; j<counter[i].length; j++){
                html.add("<td style=\"text-align:center\">" + counter[i][j] + "</td>");
            }
            html.add("</tr>");
        }
        html.add("</table>");

        return html.toString();
    }
}
