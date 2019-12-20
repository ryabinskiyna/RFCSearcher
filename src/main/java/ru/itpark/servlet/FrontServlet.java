package ru.itpark.servlet;

import ru.itpark.model.QueryModel;
import ru.itpark.repository.QueryRepository;
import ru.itpark.repository.QueryRepositorySqliteImpl;
import ru.itpark.service.QueryService;
import ru.itpark.service.QueryServiceThreadPoolImpl;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@WebServlet("/upload")
@MultipartConfig
public class FrontServlet extends HttpServlet {
    private QueryService service;
    private Path uploadPath;
    private Path resultsPath;

    protected List<String> GetFilesList(Path path)
    {
        List<String> result = new LinkedList<>();

        try (Stream<Path> walk = Files.walk(path)) {
            result = walk.filter(Files::isRegularFile)
                    .map(o -> o.toString()).collect(Collectors.toList());

            result.forEach(System.out::println);
            return result;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        System.out.println(Thread.currentThread().getName());
        String url = req.getRequestURI().substring(req.getContextPath().length());
        System.out.println(url);

        if (url.equals("/") || url.startsWith("/search")) {
            if (req.getMethod().equals("POST")) {
                final String query = req.getParameter("query");
                if (query.isEmpty()) {
                    resp.sendRedirect(req.getRequestURI());
                    return;
                }
                String id = service.search(query);
                req.setAttribute("searchid", id);
                req.getRequestDispatcher("/WEB-INF/search.jsp").forward(req, resp);
                return;
            }

            req.getRequestDispatcher("/WEB-INF/search.jsp").forward(req, resp);
        }

        if (url.startsWith("/results")) {
            final String downloadId = req.getParameter("download");

            if (downloadId != null && !downloadId.isEmpty())
            {
                String filename = downloadId + ".txt";
                resp.setContentType("APPLICATION/OCTET-STREAM");
                resp.setHeader("Content-Disposition","attachment; filename=\"" + filename + "\"");
                java.io.FileInputStream fileInputStream=new java.io.FileInputStream(resultsPath + "\\" + filename);
                ServletOutputStream out = resp.getOutputStream();
                int i;
                while ((i=fileInputStream.read()) != -1) {
                    out.write(i);
                }
                fileInputStream.close();
                out.close();
            }

            final List<QueryModel> results = service.getAll();
            // -> JSP -> foreach
            req.setAttribute("items", results);
            req.getRequestDispatcher("/WEB-INF/results.jsp").forward(req, resp);
            return;
        }

        if (url.startsWith("/upload")) {

            if (req.getMethod().equals("POST")) {
                //
                final String action = req.getParameter("action");
                if (action.equals("upload")) {
                    List<Part> fileParts = req.getParts().stream().filter(part -> "sendbtn".equals(part.getName()) && part.getSize() > 0).collect(Collectors.toList()); // Retrieves <input type="file" name="file" multiple="true">

                    for (Part filePart : fileParts) {
                        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
                        InputStream fileContent = filePart.getInputStream();
                        Path up = Paths.get(uploadPath.toString() + "\\" + fileName);

                        File targetFile = new File(up.toString());

                        Files.copy(
                                fileContent,
                                targetFile.toPath(),
                                StandardCopyOption.REPLACE_EXISTING);
                    }

                    resp.sendRedirect(req.getRequestURI());
                    return;
                }
            }

            List<String> fileNames = GetFilesList(uploadPath);
            req.setAttribute("items", fileNames);
            req.getRequestDispatcher("/WEB-INF/upload.jsp").forward(req, resp);
            return;
        }
    }

    @Override
    public void init() throws ServletException {
        // 1. DataSource
        // 2. Repository
        // 3. ExecutorService
        // 4. Service
        try {
            InitialContext context = new InitialContext();
            final DataSource dataSource = (DataSource) context.lookup("java:/comp/env/jdbc/db");
            final QueryRepository repository = new QueryRepositorySqliteImpl(dataSource);

            String uploadPathEnv = "C:\\temp\\upload";

            try {
                uploadPathEnv = System.getenv("UPLOAD_PATH");
            }
            catch (NullPointerException e)
            {
                e.printStackTrace();
            }

            uploadPath = Paths.get(uploadPathEnv);
            if (Files.notExists(uploadPath)) {
                Files.createDirectory(uploadPath);
            }
            System.out.println(uploadPath);

            String resultsPathEnv = "C:\\temp\\results";

            try {
                resultsPathEnv = System.getenv("RESULTS_PATH");
            }
            catch (NullPointerException e)
            {
                e.printStackTrace();
            }

            resultsPath = Paths.get(resultsPathEnv);
            if (Files.notExists(resultsPath)) {
                Files.createDirectory(resultsPath);
            }
            System.out.println(resultsPath);

            // можно создавать ExecutroServide прямо здесь, а можно внутри сервиса - тогда получится сделать несколько реализаций сервиса:
            // - однопоточную блокирующую
            // - многопоточную
            service = new QueryServiceThreadPoolImpl(repository, uploadPath.toString(), resultsPath.toString());
            service.init();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
      service.destroy();
    }
}
