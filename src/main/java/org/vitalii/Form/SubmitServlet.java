package org.vitalii.Form;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "submitServlet", urlPatterns = {"/submit"})
public class SubmitServlet extends HttpServlet {

    private static final File statFolder = new File("statistic");
    private static final File answers = new File(statFolder, "answers.txt");
    private static final File users = new File(statFolder, "users.txt");

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");

        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String age = req.getParameter("age");

        StringJoiner user = new StringJoiner(" ");
        user.add(firstName).add(lastName).add(age).add(System.lineSeparator());

        StringJoiner answer = new StringJoiner(" ");
        for(int i=1; true; i++){
            String currentAnswer = req.getParameter("question"+i);
            if(currentAnswer == null || currentAnswer.equals("")){
                break;
            }
            answer.add(currentAnswer);
        }
        answer.add(System.lineSeparator());

        writeInfo(answers, answer.toString(), users, user.toString());

        HttpSession session = req.getSession(true);
        session.setAttribute("user_firstName", firstName);
        session.setAttribute("user_lastName", lastName);

        resp.sendRedirect("form.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession(false);

        if(action.equals("retry") && (session != null)){
            session.removeAttribute("user_firstName");
            session.removeAttribute("user_lastName");
        }

        resp.sendRedirect("form.jsp");
    }

    private void writeInfo(File answers, String answer, File users, String user){
        if(!statFolder.exists()){
            statFolder.mkdirs();
        }

        try(FileOutputStream outAnswers = new FileOutputStream(answers, true);
            FileOutputStream outUsers = new FileOutputStream(users, true)){
            byte[] writeArray = answer.getBytes(StandardCharsets.UTF_8);
            outAnswers.write(writeArray);
            writeArray = user.getBytes(StandardCharsets.UTF_8);
            outUsers.write(writeArray);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}