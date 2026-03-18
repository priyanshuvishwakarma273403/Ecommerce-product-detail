package servlet;

import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Product;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/products", "/products/*"})
public class ProductServlet extends HttpServlet {
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action  = req.getParameter("action");
        if(action == null) action = "list";

        try{
            switch (action) {
                case "list":
                    listProducts(request, response);
                    break;
                case "view":
                    viewProduct(request, response);
                    break;
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteProduct(request, response);
                    break;
                case "search":
                    searchProducts(request, response);
                    break;
                default:
                    listProducts(request, response);
            }
        } catch (Exception e){
            req.setAttribute("error", "An error occurred: " + e.getMessage());
            req.getRequestDispatcher("/error/500.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action  = req.getParameter("action");

        try{
            switch (action){
                case "create":
                    createProduct(req, resp);
                    break;
                case "update":
                    updateProduct(req, resp);
                    break;
                default:
                    listProducts(req, resp);
            }
        } catch (Exception e){
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/products/form.jsp").forward(req, resp);
        }
    }

    public void listProducts(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = 1;
        int pageSize = 5;

        try{
            page = Integer.parseInt(req.getParameter("page"));
        } catch(NumberFormatException e){}

        List<Product> products = productDAO.getProducts(page, pageSize);
        int totalCount = productDAO.getTotalCount();
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        req.setAttribute("products", products);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalCount", totalCount);
        req.getRequestDispatcher("/products/list.jsp").forward(req, resp);

    }
}
