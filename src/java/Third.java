/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;
import com.sendgrid.*;
/**
 *
 * @author Yusef
 */
public class Third extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        //Store the input data from the contact form
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String message = request.getParameter("message");
        
        try {
            //Ensure that no field is left empty
            //TODO: Find way to determine incorrect emails
            if(name != null && !name.isEmpty() 
                    && email != null && !email.isEmpty() 
                    && message != null && !message.isEmpty()){
                //Create class object and use to send mail via sendgrid
                Third sender = new Third();
                sender.sendMail(name, email, message);

            } else {
                RequestDispatcher rd = request.getRequestDispatcher("form.jsp");
                rd.include(request,response);
                }
        } finally {
            out.close();
        }
    }
    
    /**
     * Sends an email message to the given email
     *
     * @param name the person to send the email to/the subject header
     * @param email the email address to which the mail is directed
     * @param message the content of the email
     * @return details 
     * @throws IOException if an I/O error occurs
     */
    public String sendMail(String name, String email, String message) throws IOException{    
        //Set email parameters
        Email from = new Email("jane.contactform@gmail.com");
        from.setName("Jane");
        String subject = name;
        Email to = new Email(email);
        to.setName(name);
        Content content = new Content("text/plain", message);
        Mail mail = new Mail(from, subject, to, content);
        
        //TODO: Keep API Key private
        //SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        SendGrid sg = new SendGrid("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");
        Request request = new Request();
        String details = "None";
        try {
            request.method = Method.POST;
            request.endpoint = "mail/send";
            request.body = mail.build();
            Response response = sg.api(request);
            details = response.statusCode + ' ' + response.body + ' '
                    + response.headers;
          } catch (IOException ex) {
            throw ex;
          }
        return details;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}