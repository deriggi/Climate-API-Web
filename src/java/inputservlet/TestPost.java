/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inputservlet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import service.ClimateExchangeService;

/**
 *
 * @author wb385924
 */
public class TestPost extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
//    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        PrintWriter out = response.getWriter();
//        ServletInputStream is = null;
//        BufferedReader br = null;
//        try {
//            XStream xsteam = new XStream();
//            is = request.getInputStream();
//            br = new BufferedReader(new InputStreamReader(is));
//            String line = null;
//            StringBuilder sb =  new StringBuilder();
//            while ((line = br.readLine()) != null) {
//                sb.append(line);
//            }
//            System.out.println("output is " + sb.toString());
//
//            List<OldMonthlyAreaConfig> list = (List<OldMonthlyAreaConfig>) xsteam.fromXML(sb.toString());
//            System.out.println("received " + list.toString() + " from  " + request.getRemoteAddr());
//            out.write("thanks ed");
//        } finally {
//            out.close();
//            if (is != null) {
//                is.close();
//            }
//            if (br != null) {
//                br.close();
//            }
//        }
//    }

    private static String message = "Error during Servlet processing";
    private static String thanks = "thanks ed";
    
    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) {
     try {
            int len = req.getContentLength();
            byte[] input = new byte[len];
        
            ServletInputStream sin = req.getInputStream();
            int c, count = 0 ;
            while ((c = sin.read(input, count, input.length-count)) != -1) {
                count +=c;
            }
            sin.close();
        
            String inString = new String(input);
            int index = inString.indexOf("=");
            if (index == -1) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().print(message);
                resp.getWriter().close();
                return;
            }
            String value = inString.substring(index + 1);
            
            //decode application/x-www-form-urlencoded string
            String decodedString = URLDecoder.decode(value, "UTF-8");
            
            //reverse the String
            String reverseStr = decodedString;
            
//            List<OldMonthlyAreaConfig> configs = (List<OldMonthlyAreaConfig>)new XStream().fromXML(decodedString);
//            System.out.println(configs.toString());
            ClimateExchangeService.get().saveConfigs(decodedString);

            // set the response code and write the response data
            resp.setStatus(HttpServletResponse.SC_OK);
            OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream());
            
            writer.write(thanks);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            try{
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().print(e.getMessage());
                resp.getWriter().close();
            } catch (IOException ioe) {
            }
        }
    
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
