<?php
// === FEHLERBEHANDLUNG ===
ini_set('display_errors', 1);
error_reporting(E_ALL);
set_error_handler(function ($severity, $message, $file, $line) {
    http_response_code(500);
    echo json_encode(['status' => 'error', 'message' => "PHP Error: $message in $file on line $line"]);
    exit;
});

header('Content-Type: application/json');

// Konfigurationen laden
$db_config = require __DIR__ . '/../db_config.php';
$key_base64 = require __DIR__ . '/../app_key.php';

// Daten von der App empfangen
$json_data = file_get_contents('php://input');
$data = json_decode($json_data);

if (!$data || !isset($data->email) || !isset($data->password)) {
    http_response_code(400);
    echo json_encode(['status' => 'error', 'message' => 'E-Mail oder Passwort fehlt.']);
    exit;
}
$email = trim($data->email);
$password = trim($data->password);

// DB-Passwort entschlüsseln
try {
    $key = base64_decode(str_replace('base64:', '', $key_base64));
    $iv = base64_decode(str_replace('base64:', '', $db_config['iv']));
    $tag = base64_decode(str_replace('base64:', '', $db_config['tag']));
    $encrypted_pass = base64_decode(str_replace('base64:', '', $db_config['pass_encrypted']));
    $decrypted_pass = openssl_decrypt($encrypted_pass, $db_config['cipher'], $key, OPENSSL_RAW_DATA, $iv, $tag);
    if ($decrypted_pass === false) throw new Exception("Entschlüsselung fehlgeschlagen.");
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode(['status' => 'error', 'message' => 'Server-Konfigurationsfehler.']);
    exit;
}

// Mit der Datenbank verbinden
try {
    $pdo = new PDO("mysql:host={$db_config['host']};dbname={$db_config['dbname']};charset=utf8", $db_config['user'], $decrypted_pass);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(['status' => 'error', 'message' => 'DB-Verbindung fehlgeschlagen: ' . $e->getMessage()]);
    exit;
}