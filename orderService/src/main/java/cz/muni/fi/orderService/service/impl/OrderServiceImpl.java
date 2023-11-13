package cz.muni.fi.orderService.service.impl;

import cz.muni.fi.orderService.dto.PriceDTO;
import cz.muni.fi.orderService.entity.Order;
import cz.muni.fi.orderService.entity.OrderItem;
import cz.muni.fi.orderService.enums.Currency;
import cz.muni.fi.orderService.enums.OrderState;
import cz.muni.fi.orderService.exception.EshopServiceException;
import cz.muni.fi.orderService.repository.OrderItemRepository;
import cz.muni.fi.orderService.repository.OrderRepository;
import cz.muni.fi.orderService.service.OrderService;
import cz.muni.fi.orderService.utils.Transition;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Implementation of the {@link OrderService}. This class is part of the service
 * module of the application that provides the implementation of the business
 * logic (main logic of the application).
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
	private OrderRepository orderRepository;

    @Override
    public void createOrder(Order order) {
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItemRepository.save(orderItem);
        }
        orderRepository.save(order);
    }

	@Override
	public PriceDTO getTotalPrice(long orderId, Currency currency) throws IOException {
		Optional<Order> order = orderRepository.findById(orderId);
		if (order.isEmpty()) {
			throw new EshopServiceException("Order with given id does not exist"); //TODO toto je pridane, skontroluj ci to niekede vyhadzuje
		}
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderItem item : order.get().getOrderItems()) {

			URL url = new URL("http://localhost:8083/eshop-rest/products/" + item.getProductId() + "/currentPrice");
			HttpURLConnection con = createConnection(url);
			String response = readResponse(con);
			PriceDTO pricePerItem = getPriceFromResponse(response);

            BigDecimal itemPrice = pricePerItem.getValue().multiply(new BigDecimal(item.getAmount()));
            Currency itemCurrency = pricePerItem.getCurrency();
            if(itemCurrency != currency) {
				url = new URL("http://localhost:8083/eshop-rest/products/getCurrencyRate/" + itemCurrency + "/" + currency);
				con = createConnection(url);
				response = readResponse(con);
				BigDecimal currencyRate = new BigDecimal(response);
                itemPrice = itemPrice.multiply(currencyRate);
            }
            totalPrice = totalPrice.add(itemPrice);
        }
        PriceDTO price = new PriceDTO();
        price.setCurrency(currency);
        price.setValue(totalPrice);
        return price;
    }

	@Override
	public List<Order> getAllOrdersLastWeek(OrderState state) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_YEAR, -7);
		Date lastWeek = calendar.getTime();
        return orderRepository.getOrdersCreatedBetween(lastWeek, new Date(), state);
	}

	/**
	 * The only allowed changes of state are: RECEIVED - CANCELED,
	 * RECEIVED - SHIPPED, SHIPPED - DONE
	 */
	private final Set<Transition> allowedTransitions = new HashSet<>();
	{
		allowedTransitions.add(new Transition(OrderState.RECEIVED,
				OrderState.SHIPPED));
		allowedTransitions.add(new Transition(OrderState.RECEIVED,
				OrderState.CANCELED));
		allowedTransitions.add(new Transition(OrderState.SHIPPED,
				OrderState.DONE));
	}

	@Override
	public void shipOrder(Order order) {
		checkTransition(order.getState(), OrderState.SHIPPED);
		order.setState(OrderState.SHIPPED);
	}

	@Override
	public void finishOrder(Order order) {
		checkTransition(order.getState(), OrderState.DONE);
		order.setState(OrderState.DONE);
	}

	@Override
	public void cancelOrder(Order order) {
		checkTransition(order.getState(), OrderState.CANCELED);
		order.setState(OrderState.CANCELED);
	}

	private void checkTransition(OrderState oldState, OrderState newState) {
		if (!allowedTransitions.contains(new Transition(oldState, newState)))
			throw new EshopServiceException("The transition from: " + oldState
					+ " to " + newState + " is not allowed!");
	}

	private HttpURLConnection createConnection(URL url) throws IOException {
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod(HttpMethod.GET.name());
		return con;
	}

	private String readResponse(HttpURLConnection con) throws IOException{
		if (con.getResponseCode() != HttpServletResponse.SC_OK) {
			throw new EshopServiceException("Can't get product's current price");
		}
		String inputLine;
		StringBuilder content;

		try(BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()))) {
			content = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
		} catch (Exception e) {
			throw new EshopServiceException("Can't read response");
		} finally {
			con.disconnect();
		}
		return content.toString();
	}

	private PriceDTO getPriceFromResponse(String response) {
		String[] parts = response.split(":");
		if (parts.length != 8) {
			throw new EshopServiceException("Unexpected response");
		}

		PriceDTO price = new PriceDTO();

		if (parts[7].contains(Currency.CZK.name())) {
			price.setCurrency(Currency.CZK);
		} else if (parts[7].contains(Currency.EUR.name())) {
			price.setCurrency(Currency.EUR);
		} else if (parts[7].contains(Currency.USD.name())) {
			price.setCurrency(Currency.USD);
		} else {
			throw new EshopServiceException("Unexpected response");
		}

		price.setValue(new BigDecimal(parts[2].substring(0, parts[2].indexOf(","))));
		return price;
	}
}
