// ═══════════════════════════════════════════════
//  Tortuga Map — admin_config.js
//  Доступ к этому файлу заблокирован через .htaccess
//  (Deny from all) — браузер не получает его содержимое.
//
//  КАК СМЕНИТЬ ПАРОЛЬ:
//  1. Сгенерируй новый SHA-256 хэш:
//     node -e "require('crypto').createHash('sha256').update('НовыйПароль').digest('hex')|0" | cat
//     Или онлайн: https://emn178.github.io/online-tools/sha256.html
//  2. Вставь хэш ниже в поле pwHash
//  3. Обнови .htpasswd на сервере (htpasswd -c /путь/.htpasswd admin)
// ═══════════════════════════════════════════════

const ADMIN_CFG = {
  // SHA-256 от пароля (второй фактор защиты — уже за Basic Auth)
  pwHash: "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3",
  // Максимум попыток перед блокировкой сессии
  maxAttempts: 3,
  // Время блокировки в минутах
  lockMinutes: 10,
};
