package rpc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Item;
import external.TicketMasterAPI;

/**
 * Servlet implementation class SearchItem
 */
@WebServlet("/search")
public class SearchItem extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		double lat = Double.parseDouble(request.getParameter("lat"));
		double lon = Double.parseDouble(request.getParameter("lon"));
		// Term can be empty or null.
		String term = request.getParameter("term");
		
		String userId = request.getParameter("user_id");

		DBConnection connection = DBConnectionFactory.getConnection();
		try {
			List<Item> items = connection.searchItems(lat, lon, term);
			
			Set<String> favoriteItems = connection.getFavoriteItemIds(userId);

			JSONArray array = new JSONArray();
			for (Item item : items) {
				JSONObject obj = item.toJSONObject();
				obj.put("favorite", favoriteItems.contains(item.getItemId()));
				array.put(obj);
			}
			RpcHelper.writeJSONArray(response, array);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}

	}

//		response.getWriter().append("Served at: ").append(request.getContextPath());
//		response.setContentType("text/html");
//		response.setContentType("application/json");
//		PrintWriter out = response.getWriter();
//		if (request.getParameter("username")!=null) {
//			String username = request.getParameter("username");
////			out.println("<html><body>");
////			out.println("<h1>Hello "+ username +"</h1>");
////			out.println("</body><html>");
//			
////			JSONObject obj = new JSONObject();
//			
//			JSONArray array = new JSONArray();
//			try {
////				obj.put("username", username);
//				
//				array.put(new JSONObject().put("username","abcd"));
//				array.put(new JSONObject().put("username", "1234"));
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
////			out.print(obj);
//			out.print(array);
//			out.close();
//			RpcHelper.writeJSONArray(response, array);
//			RpcHelper.writeJSONObject(response, obj);

//		out.close();

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
