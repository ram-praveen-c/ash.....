import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Main extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>Welcome to Movie Search</h1>");
        out.println("</body></html>");

        String movieTitle = request.getParameter("title");
        int release_year = Integer.parseInt(request.getParameter("year"));
        if(movieTitle.isEmpty() || release_year < 1900 || release_year > 2024) {
            out.println("<html><body>");
            out.println("<h2>Invalid input. Please enter a valid movie title and release year between 1900 and 2024.</h2>");
            out.println("</body></html>");
        } else {
            out.println("<html><body>");
            out.println("<h2>Searching for movie: " + movieTitle + " released in " + release_year + "</h2>");
            out.println("</body></html>");
        }
    }

}
