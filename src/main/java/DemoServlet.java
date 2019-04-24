import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet("/DemoServlet")
public class DemoServlet extends HttpServlet {


    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        Float sum = getPersistentSum();
        Float maximum = getPersistentMaximum();
        Integer enterCount = getPersistentEnterCount();
        String body = getBody( request ); System.out.println( body );
        String[] params = body.split(",");
        String event = params[0];
        String priceString = params[5];



        if ( ! "_".equals( priceString ) ){
            // strip € in front, parse the number behind
            float price = Float.parseFloat( priceString.split(" ")[2] );
            sum += price;
            if (maximum<price) getApplication().setAttribute("maximum", price );
            // store sum persistently in ServletContext
            getApplication().setAttribute("sum", sum );

        }

        if (event.equals("enter")){
           ++enterCount;
           getApplication().setAttribute("enterCount", enterCount);
        }



        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println( sum );
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String[] requestParamString = req.getQueryString().split("=");
        String command = requestParamString[0];
        String param = requestParamString[1];

        if ( "fun".equals( command )) {
            if ("sum".equals(param)) {
                Float sum = getPersistentSum();

                resp.setContentType("text/html");
                PrintWriter out = resp.getWriter();
                out.println(sum);

                System.out.println("sum = " + sum);
            } else if ("average".equals(param)) {
                Float sum = getPersistentSum();
                Integer enterCount = getPersistentEnterCount();
                Float average = 0.0f;

                resp.setContentType("text/html");
                PrintWriter out = resp.getWriter();

                if (enterCount != 0) average = sum / enterCount;

                out.println(average);

                System.out.println("average = " + average);

            } else if ("maximum".equals(param)) {
                Float maximum =getPersistentMaximum();

                resp.setContentType("text/html");
                PrintWriter out = resp.getWriter();
                out.println(maximum);

                System.out.println("maximum = " + maximum);

            } else {
                System.out.println( "Invalid Parameter: " + req.getQueryString() );
            }
        } else {
            System.out.println( "Invalid Command: " + req.getQueryString() );
        }
    }

    private ServletContext getApplication(){
        return getServletConfig().getServletContext();
    }

    private Float getPersistentSum(){
        Float sum;
        ServletContext application = getApplication();
        sum = (Float)application.getAttribute("sum");
        if ( sum == null ) sum = 0.0f;
        return sum;
    }

    private Float getPersistentMaximum(){
        Float maximum;
        ServletContext application = getApplication();
        maximum = (Float)application.getAttribute("maximum");
        if ( maximum == null ) maximum = 0.0f;
        return maximum;
    }

    private Integer getPersistentEnterCount() {
        Integer count;
        ServletContext application = getApplication();
        count = (Integer)application.getAttribute("enterCount");
        if(count == null){count = 0;}
        return count;

    }

    private static String getBody(HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
        return stringBuilder.toString();
    }
}
