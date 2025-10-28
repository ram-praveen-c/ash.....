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


import java.io.*;
import java.net.*;

public class MovieSearchServer {
    public static void main(String[] args) throws IOException {
        int port = 9091;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Movie Search Server running on http://localhost:" + port);

        while (true) {
            Socket socket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String requestLine;
            String query = "";
            while ((requestLine = in.readLine()) != null && !requestLine.isEmpty()) {
                if (requestLine.startsWith("GET")) {
                    int qIndex = requestLine.indexOf("?");
                    if (qIndex != -1) {
                        int end = requestLine.indexOf(" ", qIndex);
                        query = requestLine.substring(qIndex + 1, end);
                    }
                }
            }

            String title = "", yearStr = "", msg = "";
            int year = -1;
            if (!query.isEmpty()) {
                String[] params = query.split("&");
                for (String p : params) {
                    String[] kv = p.split("=");
                    if (kv.length == 2) {
                        if (kv[0].equals("title")) title = kv[1].replace("+", " ");
                        if (kv[0].equals("year")) yearStr = kv[1];
                    }
                }

                try {
                    if (!yearStr.isEmpty()) {
                        year = Integer.parseInt(yearStr);
                    }
                } catch (NumberFormatException e) {
                    year = -1;
                }

                if (title.isEmpty() || year < 1900 || year > 2024) {
                    msg = "Invalid input. Please enter a valid movie title and release year between 1900 and 2024.";
                } else {
                    msg = "Searching for movie: " + title + " released in " + year;
                }
            }

            String html = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Movie Search</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            margin: 20px;
                            background-color: #f4f4f4;
                        }
                        #container {
                            max-width: 600px;
                            margin: auto;
                            padding: 20px;
                            background: white;
                            border-radius: 8px;
                            box-shadow: 0 0 10px rgba(0,0,0,0.1);
                        }
                        #search-section {
                            margin-bottom: 20px;
                        }
                        #results-section {
                            margin-top: 20px;
                        }
                        input[type="text"] {
                            width: calc(100% - 22px);
                            padding: 10px;
                            margin-bottom: 10px;
                            border: 1px solid #ccc;
                            border-radius: 4px;
                        }
                        button {
                            padding: 10px 15px;
                            background-color: #28a745;
                            color: white;
                            border: none;
                            border-radius: 4px;
                            cursor: pointer;
                        }
                        button:hover {
                            background-color: #218838;
                        }
                    </style>
                </head>
                <body>
                    <div id="container">
                        <h2>Movie Searcher</h2>
                        <div id="search-section">
                            <form method="GET" action="/">
                                <input type="text" name="title" placeholder="Enter movie name" required>
                                <input type="text" name="year" placeholder="Enter release year" required>
                                <button type="submit">Search</button>
                            </form>
                        </div>
                        <div id="results-section">
                            <h3>Search Results:</h3>
                            <div id="results">
                """;

            if (!msg.isEmpty()) {
                html += "<p>" + msg + "</p>";
            }

            html += """
                            </div>
                        </div>
                    </div>
                </body>
                </html>
                """;

            out.write("HTTP/1.1 200 OK\r\n");
            out.write("Content-Type: text/html\r\n");
            out.write("Content-Length: " + html.length() + "\r\n\r\n");
            out.write(html);
            out.flush();
            socket.close();
        }
    }
}

