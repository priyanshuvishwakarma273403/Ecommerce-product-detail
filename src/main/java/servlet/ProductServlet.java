package servlet;

import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Product;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
                    listProducts(req, resp);
                    break;
                case "view":
                    viewProduct(req, resp);
                    break;
                case "new":
                    showNewForm(req, resp);
                    break;
                case "edit":
                    showEditForm(req, resp);
                    break;
                case "delete":
                    deleteProduct(req, resp);
                    break;
                case "search":
                    searchProducts(req, resp);
                    break;
                default:
                    listProducts(req, resp);
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


    public void viewProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Optional<Product> product = productDAO.getProductById(id);

        if(product.isPresent()){
            req.setAttribute("product", product.get());
            req.getRequestDispatcher("/products/view.jsp").forward(req, resp);
        }else{
            req.setAttribute("error", "Product not found with ID: " + id);
            resp.sendRedirect(req.getContextPath() + "/products?action=list");
        }
    }

    public void showNewForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("formTitle", "Add New Product");
        req.setAttribute("formAction", "create");
        req.getRequestDispatcher("/products/form.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Optional<Product> product = productDAO.getProductById(id);

        if(product.isPresent()){
            req.setAttribute("product", product.get());
            req.setAttribute("formTitle", "Edit Product");
            req.setAttribute("formAction", "update");
            req.getRequestDispatcher("/products/form.jsp").forward(req, resp);
        } else{
            resp.sendRedirect(req.getContextPath() + "/products?action=list");
        }
    }

    private void createProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name").trim();
        String description = req.getParameter("description").trim();
        String category = req.getParameter("category").trim();
        String priceStr = req.getParameter("price").trim();
        String quantityStr = req.getParameter("quantity").trim();

        StringBuilder errors = new  StringBuilder();
        if(name.isEmpty()) errors.append("Name is required.");
        if(priceStr.isEmpty()) errors.append("Price is required.");

        if(errors.length() > 0){
            req.setAttribute("error", errors.toString());
            req.setAttribute("formTitle", "Add New Product");
            req.setAttribute("formAction", "create");
            req.getRequestDispatcher("/products/form.jsp").forward(req, resp);
            return;
        }
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setCategory(category);
        product.setPrice(new BigDecimal(priceStr));
        product.setQuantity(Integer.parseInt(quantityStr));
        product.setActive(req.getParameter("active") != null);

        if(productDAO.addProduct(product)){
            req.getSession().setAttribute("message", "Product created successfully!");
        } else{
            req.getSession().setAttribute("error", "Failed to create product.");
        }
        resp.sendRedirect(req.getContextPath() + "/products?action=list");
    }

    private void updateProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Product product = new Product();
        product.setId(id);
        product.setName(req.getParameter("name").trim());
        product.setDescription(req.getParameter("description").trim());
        product.setCategory(req.getParameter("category").trim());
        product.setPrice(new BigDecimal(req.getParameter("price").trim()));
        product.setQuantity(Integer.parseInt(req.getParameter("quantity").trim()));
        product.setActive(req.getParameter("active") != null);


        if(productDAO.updateProduct(product)){
            req.getSession().setAttribute("message", "Product updated successfully!");
        } else{
            req.getSession().setAttribute("error", "Failed to update product.");
        }

        resp.sendRedirect(req.getContextPath() + "/products?action=list");
    }

    public void deleteProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        if(productDAO.deleteProduct(id)){
            req.getSession().setAttribute("message", "Product deleted successfully!");
        } else{
            req.getSession().setAttribute("error", "Failed to delete product.");
        }

        resp.sendRedirect(req.getContextPath() + "/products?action=list");
    }

    public void searchProducts(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("keyword");
        List<Product> products = productDAO.searchProducts(keyword);
        req.setAttribute("products", products);
        req.setAttribute("keyword", keyword);
        req.setAttribute("currentPage", 1);
        req.setAttribute("totalPages",1);
        req.setAttribute("totalCount", products.size());

        req.getRequestDispatcher("/products/list.jsp").forward(req, resp);
    }
}
