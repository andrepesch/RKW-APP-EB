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

                    <label for="legalForm">Rechtsform:</label>
                    <input type="text" id="legalForm" name="legalForm">

                    <label for="foundationDate">Gründungsdatum:</label>
                    <input type="text" id="foundationDate" name="foundationDate">

                    <label for="streetAndNumber">Straße und Hausnummer:</label>
                    <input type="text" id="streetAndNumber" name="streetAndNumber">

                    <label for="postalCode">PLZ:</label>
                    <input type="text" id="postalCode" name="postalCode">

                    <label for="city">Ort:</label>
                    <input type="text" id="city" name="city">

                    <span>Vorsteuerabzugsberechtigt?</span>
                    <label><input type="radio" name="isVatDeductible" value="true"> Ja</label>
                    <label><input type="radio" name="isVatDeductible" value="false"> Nein</label>

                    <label for="industrySector">Branche:</label>
                    <input type="text" id="industrySector" name="industrySector">

                    <label><input type="checkbox" id="hasWebsite" name="hasWebsite"> Website vorhanden</label>
                    <label for="websiteUrl">Website/URL:</label>
                    <input type="text" id="websiteUrl" name="websiteUrl">
                </div>

                <div class="form-section">
                    <h4>Ansprechpartner</h4>
                    <label for="mainContactName">Name:</label>
                    <input type="text" id="mainContactName" name="mainContactName" required>

                    <label for="mainContactEmail">E-Mail:</label>
                    <input type="email" id="mainContactEmail" name="mainContactEmail" required>

                    <label for="mainContactPhone">Telefon:</label>
                    <input type="tel" id="mainContactPhone" name="mainContactPhone">
                </div>

                <div class="form-section">
                    <h4>Wirtschaftlich Berechtigte Personen</h4>
                    <div id="owners-container"></div>
                    <button type="button" id="add-owner">Weitere Person hinzufügen</button>
                </div>

                <div class="form-section">
                    <h4>Bankverbindung</h4>
                    <label for="bankInstitute">Kreditinstitut:</label>
                    <input type="text" id="bankInstitute" name="bankInstitute">

                    <label for="bankIban">IBAN:</label>
                    <input type="text" id="bankIban" name="bankIban">

                    <label for="bankTaxId">USt-ID / Steuer-Nr.:</label>
                    <input type="text" id="bankTaxId" name="bankTaxId">
                </div>

                <div class="form-section">
                    <h4>KMU-Bewertung</h4>
                    <h5>Letztes Jahr</h5>
                    <label for="lastYearYear">Abschlussjahr:</label>
                    <input type="text" id="lastYearYear" name="lastYearYear">

                    <label for="lastYearEmployees">Mitarbeiter:</label>
                    <input type="number" id="lastYearEmployees" name="lastYearEmployees">

                    <label for="lastYearTurnover">Umsatz (€):</label>
                    <input type="text" id="lastYearTurnover" name="lastYearTurnover">

                    <label for="lastYearBalance">Bilanzsumme (€):</label>
                    <input type="text" id="lastYearBalance" name="lastYearBalance">

                    <h5>Vorletztes Jahr</h5>
                    <label for="penYearYear">Abschlussjahr:</label>
                    <input type="text" id="penYearYear" name="penYearYear">

                    <label for="penYearEmployees">Mitarbeiter:</label>
                    <input type="number" id="penYearEmployees" name="penYearEmployees">

                    <label for="penYearTurnover">Umsatz (€):</label>
                    <input type="text" id="penYearTurnover" name="penYearTurnover">

                    <label for="penYearBalance">Bilanzsumme (€):</label>
                    <input type="text" id="penYearBalance" name="penYearBalance">
                </div>

                <div class="form-section">
                    <h4>Details zur Beratung</h4>
                    <label for="consultFocus">Schwerpunkt:</label>
                    <input type="text" id="consultFocus" name="consultFocus">

                    <label for="consultScope">Umfang (Tage):</label>
                    <input type="number" id="consultScope" name="consultScope">

                    <label for="consultRate">Tagessatz (€):</label>
                    <input type="text" id="consultRate" name="consultRate">

                    <label for="consultEndDate">Zeitraum bis:</label>
                    <input type="text" id="consultEndDate" name="consultEndDate">

                    <label for="initialSituation">Ausgangssituation:</label>
                    <textarea id="initialSituation" name="initialSituation"></textarea>

                    <label for="consultContent">Beratungsinhalt:</label>
                    <textarea id="consultContent" name="consultContent"></textarea>
                </div>

                <div class="form-section">
                    <h4>Berater</h4>
                    <label><input type="checkbox" id="hasChosenConsultant" name="hasChosenConsultant"> Ich habe bereits ein Beratungsunternehmen gewählt</label>
                    <label for="consultingFirm">Beratungsfirma:</label>
                    <input type="text" id="consultingFirm" name="consultingFirm">
                    <div id="consultants-container"></div>
                    <button type="button" id="add-consultant">Weitere Berater/in hinzufügen</button>
                </div>

                <div class="form-section">
                    <label><input type="checkbox" id="hasAcknowledged" name="hasAcknowledged"> Publizitätsverpflichtungen zur Kenntnis genommen</label>
                </div>

                <button type="submit" id="submit-button">Änderungen speichern und zurücksenden</button>
            </form>
            <div id="message-container"></div>
        </main>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', async () => {
            const form = document.getElementById('customer-form');
            const loader = document.getElementById('loader');
            const messageContainer = document.getElementById('message-container');
            const submitButton = document.getElementById('submit-button');
            const ownersContainer = document.getElementById('owners-container');
            const addOwnerBtn = document.getElementById('add-owner');
            const consultantsContainer = document.getElementById('consultants-container');
            const addConsultantBtn = document.getElementById('add-consultant');

            let originalFormData = {};

            const urlParams = new URLSearchParams(window.location.search);
            const token = urlParams.get('token');

            function createOwnerFields(owner = {}) {
                const div = document.createElement('div');
                div.className = 'owner-item';
                div.innerHTML = `
                    <input type="text" class="owner-firstName" placeholder="Vorname" value="${owner.firstName || ''}">
                    <input type="text" class="owner-lastName" placeholder="Nachname" value="${owner.lastName || ''}">
                    <input type="text" class="owner-birthDate" placeholder="Geburtsdatum" value="${owner.birthDate || ''}">
                    <input type="text" class="owner-taxId" placeholder="Steuer-ID" value="${owner.taxId || ''}">
                    <button type="button" class="remove-owner">Entfernen</button>`;
                div.querySelector('.remove-owner').addEventListener('click', () => div.remove());
                return div;
            }

            function createConsultantFields(c = {}) {
                const div = document.createElement('div');
                div.className = 'consultant-item';
                div.innerHTML = `
                    <input type="text" class="consultant-firstName" placeholder="Vorname" value="${c.firstName || ''}">
                    <input type="text" class="consultant-lastName" placeholder="Nachname" value="${c.lastName || ''}">
                    <input type="text" class="consultant-accr" placeholder="Akkreditierungs-Nr." value="${c.accreditationId || ''}">
                    <input type="email" class="consultant-email" placeholder="E-Mail" value="${c.email || ''}">
                    <button type="button" class="remove-consultant">Entfernen</button>`;
                div.querySelector('.remove-consultant').addEventListener('click', () => div.remove());
                return div;
            }

            addOwnerBtn.addEventListener('click', () => ownersContainer.appendChild(createOwnerFields()));
            addConsultantBtn.addEventListener('click', () => consultantsContainer.appendChild(createConsultantFields()));

            if (!token) {
                loader.classList.add('hidden');
                messageContainer.innerHTML = '<p class="error-message">Fehler: Kein gültiger Zugriffs-Token gefunden.</p>';
                return;
            }

            try {
                const resp = await fetch(`get_form_by_token.php?token=${encodeURIComponent(token)}`);
                if (!resp.ok) throw new Error('Laden fehlgeschlagen');
                originalFormData = await resp.json();

                document.getElementById('companyName').value = originalFormData.companyName || '';
                document.getElementById('legalForm').value = originalFormData.legalForm || '';
                document.getElementById('foundationDate').value = originalFormData.foundationDate || '';
                document.getElementById('streetAndNumber').value = originalFormData.streetAndNumber || '';
                document.getElementById('postalCode').value = originalFormData.postalCode || '';
                document.getElementById('city').value = originalFormData.city || '';
                if (originalFormData.isVatDeductible) {
                    form.querySelector('input[name="isVatDeductible"][value="true"]').checked = true;
                } else {
                    form.querySelector('input[name="isVatDeductible"][value="false"]').checked = true;
                }
                document.getElementById('industrySector').value = originalFormData.industrySector || '';
                document.getElementById('hasWebsite').checked = originalFormData.hasWebsite || false;
                document.getElementById('websiteUrl').value = originalFormData.websiteUrl || '';

                document.getElementById('mainContactName').value = originalFormData.mainContact?.name || '';
                document.getElementById('mainContactEmail').value = originalFormData.mainContact?.email || '';
                document.getElementById('mainContactPhone').value = originalFormData.mainContact?.phone || '';

                (originalFormData.beneficialOwners || []).forEach(o => ownersContainer.appendChild(createOwnerFields(o)));

                document.getElementById('bankInstitute').value = originalFormData.bankDetails?.institute || '';
                document.getElementById('bankIban').value = originalFormData.bankDetails?.iban || '';
                document.getElementById('bankTaxId').value = originalFormData.bankDetails?.taxId || '';

                document.getElementById('lastYearYear').value = originalFormData.smeClassification?.lastYear?.year || '';
                document.getElementById('lastYearEmployees').value = originalFormData.smeClassification?.lastYear?.employees || '';
                document.getElementById('lastYearTurnover').value = originalFormData.smeClassification?.lastYear?.turnover || '';
                document.getElementById('lastYearBalance').value = originalFormData.smeClassification?.lastYear?.balanceSheetTotal || '';

                document.getElementById('penYearYear').value = originalFormData.smeClassification?.penultimateYear?.year || '';
                document.getElementById('penYearEmployees').value = originalFormData.smeClassification?.penultimateYear?.employees || '';
                document.getElementById('penYearTurnover').value = originalFormData.smeClassification?.penultimateYear?.turnover || '';
                document.getElementById('penYearBalance').value = originalFormData.smeClassification?.penultimateYear?.balanceSheetTotal || '';

                document.getElementById('consultFocus').value = originalFormData.consultationDetails?.focus || '';
                document.getElementById('consultScope').value = originalFormData.consultationDetails?.scopeInDays || '';
                document.getElementById('consultRate').value = originalFormData.consultationDetails?.dailyRate || '';
                document.getElementById('consultEndDate').value = originalFormData.consultationDetails?.endDate || '';
                document.getElementById('initialSituation').value = originalFormData.consultationDetails?.initialSituation || '';
                document.getElementById('consultContent').value = originalFormData.consultationDetails?.consultationContent || '';

                document.getElementById('hasChosenConsultant').checked = originalFormData.hasChosenConsultant || false;
                document.getElementById('consultingFirm').value = originalFormData.consultingFirm || '';
                (originalFormData.consultants || []).forEach(c => consultantsContainer.appendChild(createConsultantFields(c)));

                document.getElementById('hasAcknowledged').checked = originalFormData.hasAcknowledgedPublicationObligations || false;

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

                const data = JSON.parse(JSON.stringify(originalFormData));
                data.companyName = document.getElementById('companyName').value;
                data.legalForm = document.getElementById('legalForm').value;
                data.foundationDate = document.getElementById('foundationDate').value;
                data.streetAndNumber = document.getElementById('streetAndNumber').value;
                data.postalCode = document.getElementById('postalCode').value;
                data.city = document.getElementById('city').value;
                data.isVatDeductible = form.querySelector('input[name="isVatDeductible"]:checked')?.value === 'true';
                data.industrySector = document.getElementById('industrySector').value;
                data.hasWebsite = document.getElementById('hasWebsite').checked;
                data.websiteUrl = document.getElementById('websiteUrl').value;

                data.mainContact = {
                    name: document.getElementById('mainContactName').value,
                    email: document.getElementById('mainContactEmail').value,
                    phone: document.getElementById('mainContactPhone').value
                };

                data.beneficialOwners = Array.from(ownersContainer.querySelectorAll('.owner-item')).map(div => ({
                    firstName: div.querySelector('.owner-firstName').value,
                    lastName: div.querySelector('.owner-lastName').value,
                    birthDate: div.querySelector('.owner-birthDate').value,
                    taxId: div.querySelector('.owner-taxId').value
                }));

                data.bankDetails = {
                    institute: document.getElementById('bankInstitute').value,
                    iban: document.getElementById('bankIban').value,
                    taxId: document.getElementById('bankTaxId').value
                };

                data.smeClassification = {
                    lastYear: {
                        year: document.getElementById('lastYearYear').value,
                        employees: parseInt(document.getElementById('lastYearEmployees').value || '0'),
                        turnover: document.getElementById('lastYearTurnover').value,
                        balanceSheetTotal: document.getElementById('lastYearBalance').value
                    },
                    penultimateYear: {
                        year: document.getElementById('penYearYear').value,
                        employees: parseInt(document.getElementById('penYearEmployees').value || '0'),
                        turnover: document.getElementById('penYearTurnover').value,
                        balanceSheetTotal: document.getElementById('penYearBalance').value
                    }
                };

                data.consultationDetails = {
                    focus: document.getElementById('consultFocus').value,
                    scopeInDays: parseInt(document.getElementById('consultScope').value || '0'),
                    dailyRate: document.getElementById('consultRate').value,
                    endDate: document.getElementById('consultEndDate').value,
                    initialSituation: document.getElementById('initialSituation').value,
                    consultationContent: document.getElementById('consultContent').value
                };

                data.hasChosenConsultant = document.getElementById('hasChosenConsultant').checked;
                data.consultingFirm = document.getElementById('consultingFirm').value;
                data.consultants = Array.from(consultantsContainer.querySelectorAll('.consultant-item')).map(div => ({
                    firstName: div.querySelector('.consultant-firstName').value,
                    lastName: div.querySelector('.consultant-lastName').value,
                    accreditationId: div.querySelector('.consultant-accr').value,
                    email: div.querySelector('.consultant-email').value
                }));

                data.hasAcknowledgedPublicationObligations = document.getElementById('hasAcknowledged').checked;

                try {
                    const resp = await fetch(`save_form.php?token=${encodeURIComponent(token)}`, {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify(data)
                    });

                    if (!resp.ok) throw new Error('Speichern fehlgeschlagen');

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