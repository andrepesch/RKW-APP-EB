<?php
// delete_form.php
// Löscht einen Erfassungsbogen anhand seiner ID.

ini_set('display_errors', 1);
error_reporting(E_ALL);
set_error_handler(function ($severity, $message, $file, $line) {
    http_response_code(500);
    echo json_encode(['status' => 'error', 'message' => "PHP Error: $message in $file on line $line"]);
    exit;
});

header('Content-Type: application/json');

$db_config = require __DIR__ . '/../db_config.php';
$key_base64 = require __DIR__ . '/../app_key.php';

if (!isset($_GET['form_id']) || !is_numeric($_GET['form_id'])) {
    http_response_code(400);
    echo json_encode(['status' => 'error', 'message' => 'Gültige Formular-ID fehlt.']);
    exit;
}
$form_id = (int)$_GET['form_id'];

try {
    $key = base64_decode(str_replace('base64:', '', $key_base64));
    $iv = base64_decode(str_replace('base64:', '', $db_config['iv']));
    $tag = base64_decode(str_replace('base64:', '', $db_config['tag']));
    $encrypted_pass = base64_decode(str_replace('base64:', '', $db_config['pass_encrypted']));
    $decrypted_pass = openssl_decrypt($encrypted_pass, $db_config['cipher'], $key, OPENSSL_RAW_DATA, $iv, $tag);
    if ($decrypted_pass === false) throw new Exception('DB Passwort Entschlüsselung fehlgeschlagen.');

    $pdo = new PDO("mysql:host={$db_config['host']};dbname={$db_config['dbname']};charset=utf8", $db_config['user'], $decrypted_pass);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode(['status' => 'error', 'message' => 'Server-Fehler: ' . $e->getMessage()]);
    exit;
}

try {
    $stmt = $pdo->prepare("DELETE FROM formulare WHERE id = ?");
    $stmt->execute([$form_id]);

    if ($stmt->rowCount() === 0) {
        http_response_code(404);
        echo json_encode(['status' => 'error', 'message' => 'Formular nicht gefunden.']);
    } else {
        echo json_encode(['status' => 'success', 'message' => 'Formular erfolgreich gelöscht.']);
    }
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode(['status' => 'error', 'message' => 'Fehler beim Löschen: ' . $e->getMessage()]);
}
?>