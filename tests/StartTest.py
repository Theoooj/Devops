import test_login 
import time
import sys

def start_tests():
    try:
        print("Démarrage des tests Selenium...")
        # Petite attente supplémentaire pour être sûr que les services sont prêts
        time.sleep(5)
        
        # Exécution du test de login
        test_login.test_login()
        
        print("Tous les tests ont réussi!")
        return True
    except Exception as e:
        print(f"Erreur lors de l'exécution des tests: {e}")
        return False

if __name__ == "__main__":
    success = start_tests()
    sys.exit(0 if success else 1)