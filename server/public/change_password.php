<?php
ini_set('display_errors', 1);
error_reporting(E_ALL);
header('Content-Type: application/json');

$db_config = require __DIR__ . '/../db_config.php';
$key_base64 = require __DIR__ . '/../app_key.php';

$data = json_decode(file_get_contents('php://input'));
if (!$data || !isset($data->beraterId) || !isset($data->password)) {
    http_response_code(400);
    echo json_encode(['status' => 'error', 'message' => 'Ungültige Eingabedaten.']);
    exit;
}
$beraterId = $data->beraterId;
$password = trim($data->password);
if (strlen($password) < 8) {
    http_response_code(400);
    echo json_encode(['status' => 'error', 'message' => 'Passwort zu kurz.']);
    exit;
}
$password_hash = password_hash($password, PASSWORD_DEFAULT);

try {
    $key = base64_decode(str_replace('base64:', '', $key_base64));
    $iv = base64_decode(str_replace('base64:', '', $db_config['iv']));
    $tag = base64_decode(str_replace('base64:', '', $db_config['tag']));
    $encrypted_pass = base64_decode(str_replace('base64:', '', $db_config['pass_encrypted']));
    $decrypted_pass = openssl_decrypt($encrypted_pass, $db_config['cipher'], $key, OPENSSL_RAW_DATA, $iv, $tag);
    if ($decrypted_pass === false) throw new Exception('Passwort entschlüsselung fehlgeschlagen.');
    $pdo = new PDO("mysql:host={$db_config['host']};dbname={$db_config['dbname']};charset=utf8", $db_config['user'], $decrypted_pass);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    $stmt = $pdo->prepare("UPDATE berater SET password_hash = ? WHERE id = ?");
    $stmt->execute([$password_hash, $beraterId]);
    echo json_encode(['status' => 'success', 'message' => 'Passwort geändert.']);
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode(['status' => 'error', 'message' => 'Server-Fehler: ' . $e->getMessage()]);
}