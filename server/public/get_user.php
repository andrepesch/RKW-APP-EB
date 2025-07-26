<?php
ini_set('display_errors', 1);
error_reporting(E_ALL);
header('Content-Type: application/json');

$db_config = require __DIR__ . '/../db_config.php';
$key_base64 = require __DIR__ . '/../app_key.php';

if (!isset($_GET['berater_id'])) {
    http_response_code(400);
    echo json_encode(['status' => 'error', 'message' => 'Berater-ID fehlt.']);
    exit;
}
$berater_id = $_GET['berater_id'];

try {
    $key = base64_decode(str_replace('base64:', '', $key_base64));
    $iv = base64_decode(str_replace('base64:', '', $db_config['iv']));
    $tag = base64_decode(str_replace('base64:', '', $db_config['tag']));
    $encrypted_pass = base64_decode(str_replace('base64:', '', $db_config['pass_encrypted']));
    $decrypted_pass = openssl_decrypt($encrypted_pass, $db_config['cipher'], $key, OPENSSL_RAW_DATA, $iv, $tag);
    if ($decrypted_pass === false) throw new Exception('Passwort entschlüsselung fehlgeschlagen.');
    $pdo = new PDO("mysql:host={$db_config['host']};dbname={$db_config['dbname']};charset=utf8", $db_config['user'], $decrypted_pass);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    $stmt = $pdo->prepare("SELECT id, email, salutation, first_name, last_name, phone, address, photo FROM berater WHERE id = ?");
    $stmt->execute([$berater_id]);
    $user = $stmt->fetch(PDO::FETCH_ASSOC);
    if ($user) {
        $user['photo'] = $user['photo'] ? base64_encode($user['photo']) : null;
        echo json_encode($user);
    } else {
        http_response_code(404);
        echo json_encode(['status' => 'error', 'message' => 'Benutzer nicht gefunden.']);
    }
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode(['status' => 'error', 'message' => 'Server-Fehler: ' . $e->getMessage()]);
}