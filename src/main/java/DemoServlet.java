import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class DemoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String[] requestParamString = req.getQueryString().split("=");
        String command = requestParamString[0];
        String param = requestParamString[1];

        if ( "fun".equals( command ) && "sum".equals( param ) ){
            Float sum = getPersistentSum();

            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();
            out.println( sum );

            System.out.println( "sum = " + sum );
        } else {
            System.out.println( "Invalid Command: " + req.getQueryString() );
        }
    }
}
