from selenium.webdriver.common.by import By
from selenium import webdriver
import time

FRONTEND_URL = "http://frontend:4200"

def test_login():
    options = webdriver.ChromeOptions()
    options.add_argument("--headless")  
    driver = webdriver.Remote(command_executor="http://selenium:4444/wd/hub", options=options)


    try:
        driver.get(FRONTEND_URL + "/login")
        time.sleep(2)

        email_input = driver.find_element(By.ID, "email")
        password_input = driver.find_element(By.ID, "password")
        submit_button = driver.find_element(By.TAG_NAME, "button")

        email_input.send_keys("test@example.com")
        password_input.send_keys("password123")
        submit_button.click()

        time.sleep(3)

        assert "dashboard" in driver.current_url, "Redirection après connexion échouée"

        print("Test de connexion réussi §§§!")

    finally:
        driver.quit()