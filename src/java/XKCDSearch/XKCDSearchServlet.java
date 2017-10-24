/**
 *
 * @author ankurs
 * This is the servlet file for Project1Task2 which perform XKCDComic's web scraping
 * The welcome file in web.xml points to this servlet.Hence it is the starting point for the web application as well
 * 
 * The servlet is acting as a controller
 * It has two views - index.jsp and result.jsp
 * It is decided based on the search parameter. It takes index.jsp as the starting place, but when the search is performed result.jsp view is called.
 * The model is provided by XKCDSearchModel.
 */
package XKCDSearch;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

///getXKCDSearch is the default url for the servlet
@WebServlet(name = "XKCDSearchServlet",
        urlPatterns = {"/getXKCDSearch"})
public class XKCDSearchServlet extends HttpServlet {

    //the  business model for this app
    XKCDSearchModel xsm = null;
    
    //INitiate the servelt by initlantiating the model it will use
    @Override
    public void init() {
        xsm = new XKCDSearchModel();
    }
    

    // This servlet will reply to HTTP GET requests via this doGet method
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
       //get the word to be searched for the comics
       String search = request.getParameter("searchedWord");
       
       // set the view to be used
       String nextView;
       
       //while the searched  parameter is not null, perform the search
       //If it is null then prompt the  user to the welcome pasge to enter the search keyword
       if(search != null){
          //use model to do the search
          xsm.doXKCDSearch(search);
          
          //set attribute for the searched keyword
          request.setAttribute("searchWord", search);
          
          //set attribute fofr the total count of al thhe comics
          request.setAttribute("totalCount", xsm.getTotalCount());
          
          //set attriibute for the count of the searched keyword on the website, depicting the number of comics
          request.setAttribute("searchCount", xsm.getSearchCount());
          
          //if the search is a success set the usrl and comic name attribute to be displayed
          if(xsm.getSearchCount() != 0){
          
          //set url for the comic image to be displayed    
          request.setAttribute("resultUrl",xsm.getResultUrl());
          
          //set the comic name chosen randomly from the matched comics
          request.setAttribute("comicName",xsm.getComicName());
          }
          
          //set view to be the result page to display results
          nextView = "result.jsp";
       }
       else{
           //if search is not performed yet, welcome the user to perform the search
           nextView = "welcome.jsp";
       }
       
       //transfer the ocntrol to the correct view
       RequestDispatcher view = request.getRequestDispatcher(nextView);
       view.forward(request, response);
    }

}
