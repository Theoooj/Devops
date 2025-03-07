from selenium.webdriver.common.by import By
from selenium import webdriver
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import requests
import time
import os
import sys

# Utilisez localhost avec GitHub Actions, car le service est accessible sur localhost
FRONTEND_URL = os.environ.get("FRONTEND_URL", "http://localhost:4200")
SELENIUM_URL = os.environ.get("SELENIUM_URL", "http://localhost:4444/wd/hub")

def is_frontend_running():
    """Vérifie si le frontend est disponible"""
    try:
        print(f"Vérification de l'accessibilité du frontend à {FRONTEND_URL}...")
        response = requests.get(FRONTEND_URL, timeout=5)
        if response.status_code < 400:
            print(f"Frontend accessible avec le code HTTP {response.status_code}")
            return True
        else:
            print(f"Frontend a répondu avec le code HTTP {response.status_code}")
            return False
    except requests.exceptions.RequestException as e:
        print(f"Erreur lors de la vérification du frontend: {e}")
        return False

def test_login():
    # Vérification préalable du frontend
    max_attempts = 5
    attempt = 0
    while attempt < max_attempts and not is_frontend_running():
        attempt += 1
        print(f"Le frontend n'est pas accessible. Tentative {attempt}/{max_attempts}")
        if attempt == max_attempts:
            raise Exception(f"Le frontend n'est pas accessible après {max_attempts} tentatives")
        time.sleep(10)

    print(f"Connexion à Selenium sur {SELENIUM_URL}")
    
    options = webdriver.ChromeOptions()
    options.add_argument("--headless")
    options.add_argument("--no-sandbox")
    options.add_argument("--disable-dev-shm-usage")
    # Ajout d'options pour la journalisation détaillée
    options.add_argument("--verbose")
    options.add_argument("--log-level=3")  # INFO = 0, WARNING = 1, LOG_ERROR = 2, LOG_FATAL = 3
    
    # Ajouter un mécanisme de retry pour la connexion à Selenium
    max_retries = 5
    retry_interval = 10  # secondes
    
    for attempt in range(max_retries):
        try:
            print(f"Tentative de connexion à Selenium ({attempt+1}/{max_retries})...")
            driver = webdriver.Remote(command_executor=SELENIUM_URL, options=options)
            print("Connexion à Selenium réussie!")
            break
        except Exception as e:
            if attempt < max_retries - 1:
                print(f"Échec de connexion à Selenium: {e}")
                print(f"Nouvelle tentative dans {retry_interval} secondes...")
                time.sleep(retry_interval)
            else:
                print(f"Échec définitif de connexion à Selenium après {max_retries} tentatives")
                raise

    try:
        # Charger la page de login avec une vérification préalable
        print(f"Chargement de la page {FRONTEND_URL}/login")
        # Vérifier d'abord la page d'accueil pour s'assurer que le serveur est accessible
        driver.get(FRONTEND_URL)
        print(f"Page d'accueil chargée, titre: '{driver.title}'")
        
        # Puis charger la page de login
        driver.get(f"{FRONTEND_URL}/login")
        print(f"Page de login chargée, URL actuelle: {driver.current_url}")
        
        # Attendre que les éléments du formulaire soient disponibles
        wait = WebDriverWait(driver, 20)
        
        print("Recherche de l'élément email...")
        email_input = wait.until(EC.presence_of_element_located((By.ID, "email")))
        
        print("Recherche de l'élément password...")
        password_input = wait.until(EC.presence_of_element_located((By.ID, "password")))
        
        print("Recherche du bouton de soumission...")
        submit_button = wait.until(EC.element_to_be_clickable((By.TAG_NAME, "button")))

        # Remplir le formulaire
        print("Remplissage du formulaire...")
        email_input.send_keys("test@example.com")
        password_input.send_keys("password123")
        
        # Soumettre le formulaire
        print("Clic sur le bouton de soumission...")
        submit_button.click()

        # Attendre la redirection
        print("Attente de la redirection...")
        wait.until(EC.url_contains("dashboard"))

        print(f"URL actuelle: {driver.current_url}")
        assert "dashboard" in driver.current_url, "Redirection après connexion échouée"

        print("Test de connexion réussi!")

    except Exception as e:
        print(f"Erreur pendant le test: {e}")
        # Prendre une capture d'écran en cas d'échec
        try:
            screenshot_path = "/tmp/error_screenshot.png"
            driver.save_screenshot(screenshot_path)
            print(f"Capture d'écran enregistrée: {screenshot_path}")
            
            # Afficher le source HTML de la page pour le débogage
            print("\nSource HTML de la page:")
            print(driver.page_source[:1000] + "..." if len(driver.page_source) > 1000 else driver.page_source)
        except Exception as se:
            print(f"Impossible de prendre une capture d'écran ou d'obtenir le source: {se}")
        raise
    finally:
        print("Fermeture du navigateur")
        driver.quit()