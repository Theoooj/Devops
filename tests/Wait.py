from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC


driver = webdriver.Chrome()
# page générée avec js en async
driver.get("https://ludivinecrepin.fr/selenium/")

body = driver.find_element(By.TAG_NAME, 'body')
#print(body.text)

# permet d'attendre que le h2 soit chargé
wait = WebDriverWait(driver, timeout=2)
btn = wait.until(EC.presence_of_element_located((By.TAG_NAME, 'button')))
print(btn.text)
#print(body.text)
assert len(body.text) > 100
# soulève un assertion error
assert len(body.text) == 0
# fermer le navigateur
driver.close()
driver.quit()