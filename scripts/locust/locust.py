import time
from locust import HttpUser, task, between

class EshopUser(HttpUser):
    wait_time = between(1, 5) # this is how long it takes after it has visited the endpoint until it starts visiting a new one

    @task
    def getUsers(self):
        self.client.get("/users")

    @task
    def getUser(self):
        self.client.get("/users/1")

    @task
    def getCategories(self):
        self.client.get("/categories")

    @task
    def getCategory(self):
        self.client.get("/categories/1")

    @task
    def getProducts(self):
        self.client.get("/products")

    @task
    def getProduct(self):
        self.client.get("/products/1")

    @task
    def getProductPriceByProductId(self):
        self.client.get("/products/2/currentPrice")

    @task
    def getCurrencyRate(self):
        self.client.get("/products/getCurrencyRate/CZK/EUR")

    @task
    def getOrders(self):
        self.client.get("/orders?status=ALL")

    @task
    def getOrdersLastWeek(self):
        self.client.get("/orders?status=ALL&last_week=TRUE")

    @task
    def getOrdersByUserId(self):
        self.client.get("/orders/by_user_id/1")

    @task
    def getOrder(self):
        self.client.get("/orders/1")