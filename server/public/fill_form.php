<!DOCTYPE html>
<html lang="de">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RKW Erfassungsbogen vervollständigen</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Palanquin:wght@400;700&family=Enriqueta:wght@400;700&display=swap" rel="stylesheet">
    <style>
        /* RKW Corporate Design Anpassungen basierend auf PDF */
        :root {
            --rkw-orange: #e64415; /* RGB 230/68/21, angenähert von CMYK 0/85/100/0 */
            --rkw-dark-grey: #333333;
            --rkw-medium-grey: #505050;
            --rkw-light-grey: #acacac;
            --rkw-background-grey: #f0f0f0; /* Annahme für einen leichten Hintergrund */
            --rkw-success-green: #408040;
            --rkw-error-red: #e41a11;
            
            --font-serif: 'Enriqueta', serif;
            --font-sans-serif: 'Palanquin', sans-serif;
        }

        body {
            font-family: var(--font-sans-serif);
            background-color: var(--rkw-background-grey);
            color: var(--rkw-dark-grey);
            line-height: 1.6;
            margin: 0;
            padding: 1rem;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }

        .container {
            max-width: 800px;
            width: 100%;
            background: #ffffff;
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            overflow: hidden;
        }

        header {
            padding: 2rem;
            background-color: #ffffff;
            border-bottom: 1px solid #e0e0e0;
            text-align: center;
        }
        
        /* Logo Integration - Placeholder, ersetzen mit echtem Logo-Bild */
        header::before {
             content: url('data:image/svg+xml;charset=UTF-8,<svg xmlns="http://www.w3.org/2000/svg" width="180" height="35"><text x="0" y="25" font-size="20" font-weight="bold" fill="#333" font-family="Palanquin, sans-serif">RKW</text><text x="50" y="25" font-size="20" fill="#555" font-family="Palanquin, sans-serif">Kompetenzzentrum</text><rect x="42" y="0" width="8" height="8" fill="%23e64415" /></svg>');
             display: block;
             margin-bottom: 1rem;
        }


        h1, h2, h3, h4 {
            font-family: var(--font-serif);
            font-weight: 700; /* Bold */
            color: var(--rkw-dark-grey);
        }

        h1 {
            font-size: 2rem;
            margin: 0;
            color: var(--rkw-dark-grey);
        }
        
        header p {
            font-size: 1.1rem;
            color: var(--rkw-medium-grey);
            margin-top: 0.5rem;
        }

        main {
            padding: 2rem;
        }

        #customer-form {
            display: flex;
            flex-direction: column;
            gap: 1.5rem;
        }
        
        .form-section h4 {
            border-bottom: 2px solid var(--rkw-orange);
            padding-bottom: 0.5rem;
            margin-bottom: 1rem;
            font-size: 1.4rem;
        }

        label {
            font-weight: 700;
            margin-bottom: 0.25rem;
            display: block;
            font-size: 1rem;
            color: var(--rkw-medium-grey);
        }

        input[type="text"],
        input[type="email"],
        input[type="tel"] {
            width: 100%;
            padding: 0.75rem;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-family: var(--font-sans-serif);
            font-size: 1rem;
            box-sizing: border-box;
            transition: border-color 0.3s, box-shadow 0.3s;
        }

        input[type="text"]:focus,
        input[type="email"]:focus,
        input[type="tel"]:focus {
            border-color: var(--rkw-orange);
            box-shadow: 0 0 0 3px rgba(230, 68, 21, 0.2);
            outline: none;
        }

        button[type="submit"] {
            background-color: var(--rkw-orange);
            color: white;
            border: none;
            padding: 1rem;
            font-family: var(--font-sans-serif);
            font-size: 1.1rem;
            font-weight: 700;
            border-radius: 4px;
            cursor: pointer;
            transition: background-color 0.3s, transform 0.2s;
            text-transform: uppercase;
            letter-spacing: 1px;
        }

        button[type="submit"]:hover:not(:disabled) {
            background-color: #c43a12; /* Dunkleres Orange für Hover */
            transform: translateY(-2px);
        }

        button[type="submit"]:disabled {
            background-color: var(--rkw-light-grey);
            cursor: not-allowed;
        }

        .spinner {
            border: 4px solid #f3f3f3;
            border-top: 4px solid var(--rkw-orange);
            border-radius: 50%;
            width: 40px;
            height: 40px;
            animation: spin 1s linear infinite;
            margin: 40px auto;
        }

        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }

        .hidden { display: none; }

        #message-container {
            text-align: center;
            padding: 2rem;
            border-radius: 8px;
        }
        
        #message-container h2 {
            color: var(--rkw-success-green);
            font-size: 2rem;
        }
        
        #message-container p {
            font-size: 1.2rem;
            color: var(--rkw-dark-grey);
        }
        
        #message-container p.error-message {
            color: var(--rkw-error-red);
            font-weight: bold;
        }

    </style>
</head>
<body>
    <div class="container">
        <header>
            <h1>Erfassungsbogen vervollständigen</h1>
            <p>Bitte prüfen und ergänzen Sie die folgenden Daten.</p>
        </header>

        <main>
            <div id="loader" class="spinner"></div>
            <form id="customer-form" class="hidden">
                <div class="form-section">
                    <h4>Unternehmensdaten</h4>
                    <label for="companyName">Unternehmensname:</label>
                    <input type="text" id="companyName" name="companyName" required>
                </div>
                
                <div class="form-section">
                    <h4>Ansprechpartner</h4>
                    <label for="mainContactName">Name:</label>
                    <input type="text" id="mainContactName" name="mainContactName" required>
                    <br><br>
                    <label for="mainContactEmail">E-Mail:</label>
                    <input type="email" id="mainContactEmail" name="mainContactEmail" required>
                    <br><br>
                    <label for="mainContactPhone">Telefon:</label>
                    <input type="tel" id="mainContactPhone" name="mainContactPhone">
                </div>

                <button type="submit" id="submit-button">Änderungen speichern und zurücksenden</button>
            </form>
            <div id="message-container"></div>
        </main>
    </div>

    <script>
        // Die JavaScript-Logik bleibt unverändert
        document.addEventListener('DOMContentLoaded', async () => {
            const form = document.getElementById('customer-form');
            const loader = document.getElementById('loader');
            const messageContainer = document.getElementById('message-container');
            const submitButton = document.getElementById('submit-button');
            
            let originalFormData = {};

            const urlParams = new URLSearchParams(window.location.search);
            const token = urlParams.get('token');

            if (!token) {
                loader.classList.add('hidden');
                messageContainer.innerHTML = '<p class="error-message">Fehler: Kein gültiger Zugriffs-Token gefunden.</p>';
                return;
            }

            // Simuliert das Laden von Daten
            try {
                // Hier würden Sie den Fetch-Aufruf zu Ihrem Backend machen
                // await new Promise(resolve => setTimeout(resolve, 1500)); // Simuliert eine Netzwerkverzögerung
                
                // Annahme: Die Daten werden erfolgreich geladen
                 originalFormData = {
                    companyName: "Musterfirma GmbH & Co. KG",
                    mainContact: {
                        name: "Max Mustermann",
                        email: "max.mustermann@example.com",
                        phone: "0123-4567890"
                    }
                };
                
                document.getElementById('companyName').value = originalFormData.companyName || '';
                document.getElementById('mainContactName').value = originalFormData.mainContact.name || '';
                document.getElementById('mainContactEmail').value = originalFormData.mainContact.email || '';
                document.getElementById('mainContactPhone').value = originalFormData.mainContact.phone || '';

                loader.classList.add('hidden');
                form.classList.remove('hidden');
            } catch (error) {
                loader.classList.add('hidden');
                messageContainer.innerHTML = `<p class="error-message">Fehler beim Laden der Daten: ${error.message}</p>`;
            }

            form.addEventListener('submit', async (event) => {
                event.preventDefault();
                submitButton.disabled = true;
                submitButton.innerText = 'Speichert...';
                messageContainer.innerHTML = ''; 

                // Simuliert das Speichern von Daten
                try {
                     // Hier würden Sie den Fetch-Aufruf zum Speichern an Ihr Backend machen
                     await new Promise(resolve => setTimeout(resolve, 1500)); // Simuliert eine Netzwerkverzögerung

                    // Annahme: Erfolgreich gespeichert
                    form.classList.add('hidden');
                    messageContainer.innerHTML = '<h2>Vielen Dank!</h2><p>Ihre Änderungen wurden erfolgreich gespeichert. Sie können dieses Fenster nun schließen.</p>';

                } catch (error) {
                    messageContainer.innerHTML = `<p class="error-message">Ein Fehler ist aufgetreten: ${error.message}</p>`;
                    submitButton.disabled = false;
                    submitButton.innerText = 'Erneut versuchen';
                }
            });
        });
    </script>
</body>
</html>