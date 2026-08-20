package brain.tutorial;

import net.minecraftforge.common.config.Configuration;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TutorialTexts {
    public static Configuration config;
    private static final Map<String, String> texts = new HashMap<>();

    public static void setupAndLoad() {
        config = new Configuration(new File("config", "tutorial_texts.cfg"));
        load();
    }

    public static void load() {
        config.load();
        texts.clear();

        String category = "Texts";
        config.addCustomCategoryComment(category, "All text and translations for the tutorial");

        // Welcome GUI
        loadString("welcome.title", "Обучение", category);
        loadString("welcome.description", "Добро пожаловать, новичок. Сейчас начнется обучение, вы готовы?", category);
        loadString("welcome.button_queue", "Встать в очередь", category);
        
        // Queue status HUD
        loadString("queue.status", "В очереди: %d", category);

        // Stage Titles
        loadString("stage1.title", "§6Обучение: Трактирная драка", category);
        loadString("stage2.title", "§6Обучение: Разговор с Капитаном", category);
        loadString("stage3.title", "§6Обучение: Освоить меню", category);
        loadString("stage4.title", "§6Обучение: Приват территории", category);
        loadString("stage5.title", "§6Обучение: Трактирщик", category);
        loadString("stage6.title", "§6Обучение: Перековка у кузнеца", category);
        loadString("stage7.title", "§6Обучение: Боевая тренировка", category);
        loadString("stage8.title", "§6Обучение: Бунт матросов", category);
        loadString("stage9.title", "§6Обучение: Сбор ресурсов", category);
        loadString("stage10.title", "§6Обучение: Встреча с Незнакомцем", category);

        // Progress Formats
        loadString("stage1.progress", "%d/2 пьяниц убито", category);
        loadString("stage3.progress", "%d/46", category);
        loadString("stage8.progress", "%d/5 убито", category);

        // Subtitles Stage 1-2
        loadString("subtitle.stage1.drunkard1", "§c<Пьяница> Эй, ты! Что забыл в нашем трактире?!", category);
        loadString("subtitle.stage2.captain1", "§b<Капитан> Неплохо для салаги. Ты только что прошел боевое крещение.", category);
        loadString("subtitle.stage2.captain2", "§b<Капитан> Добро пожаловать на Тортугу. Первым делом изучи интерфейс.", category);
        loadString("subtitle.stage2.captain3", "§b<Капитан> Открой главное меню. Нажми клавишу L.", category);
        
        // Stage 3 Start
        loadString("subtitle.stage3.start", "§e[Обучение] Откройте игровое меню клавишей (По умолчанию E).", category);
        loadString("subtitle.stage3.captain_done", "§a[Капитан] Отлично, теперь перейдем к практике!", category);

        // Stage 5
        loadString("subtitle.stage5.bartender", "§e[Трактирщик] Купи у меня что-нибудь (ПКМ).", category);

        // Stage 6
        loadString("subtitle.stage6.start", "§e[Обучение] Подойди к бронникy (ПКМ) — он поможет зачаровать нагрудник.", category);
        loadString("subtitle.stage6.armorsmith_open", "§e[Обучение] Следуй подсказкам на экране.", category);
        loadString("subtitle.stage6.armorsmith_enchant", "§e[Обучение] Следуй подсказкам на экране.", category);
        loadString("subtitle.stage6.armorsmith_forge", "§e[Обучение] Следуй подсказкам на экране.", category);
        loadString("subtitle.stage6.anvil_start", "§e[Обучение] Нагрудник готов! Теперь иди к наковальне и зачаруй меч.", category);
        loadString("subtitle.stage6.anvil_open", "§e[Обучение] Следуй подсказкам на экране.", category);
        loadString("subtitle.stage6.anvil_enchant", "§e[Обучение] Следуй подсказкам на экране.", category);
        loadString("subtitle.stage6.anvil_forge", "§e[Обучение] Следуй подсказкам на экране.", category);
        loadString("subtitle.stage6.done", "§a[Обучение] Оружие и броня готовы, пора на корабль!", category);

        // Stage 7 – weapon dummies (9 weapons)
        loadString("subtitle.stage7.start", "§e[Капитан] Обрати внимание на свои характеристики: Здоровье, Броня, Еда.", category);
        loadString("subtitle.stage7.stamina_info", "§e[Капитан] А здесь твоя выносливость. Она тратится на бег и удары.", category);
        loadString("subtitle.stage7.run_info", "§e[Капитан] Побегай и попрыгай, чтобы потратить её! (Дважды W или Ctrl + Пробел)", category);
        loadString("subtitle.stage7.stamina_regen", "§e[Капитан] Выносливость восстанавливается стоя или идя. Можно пить зелья или есть еду!", category);
        loadString("subtitle.stage7.dagger_hit", "§e[Боцман] Возьми КИНЖАЛ и ударь манекен! Открой инвентарь — посмотри характеристики.", category);
        loadString("subtitle.stage7.sword_hit", "§e[Боцман] Теперь бери МЕЧ! Сравни урон с кинжалом.", category);
        loadString("subtitle.stage7.axe_hit", "§e[Боцман] ТОПОР — оружие с большим разбросом удара!", category);
        loadString("subtitle.stage7.hammer_hit", "§e[Боцман] МОЛОТ — медленный, но бьёт очень больно!", category);
        loadString("subtitle.stage7.battleaxe_hit", "§e[Боцман] СЕКИРА — двуручное оружие, широкий замах!", category);
        loadString("subtitle.stage7.spear_hit", "§e[Боцман] КОПЬЁ — длинный радиус, бей из-за строя!", category);
        loadString("subtitle.stage7.pike_hit", "§e[Боцман] ПИКА — ещё длиннее копья. Идеально против конницы!", category);
        loadString("subtitle.stage7.bow_hit", "§e[Боцман] ЛУК — стреляй издалека! Манекен движется — целься!", category);
        loadString("subtitle.stage7.crossbow_hit", "§e[Боцман] АРБАЛЕТ — мощнее лука, но перезаряжается дольше. Огонь!", category);
        // Stage 7 – block fights (3 rounds)
        loadString("subtitle.stage7.block_info", "§e[Боцман] А сейчас попробуй блокировать удар! Держи ПКМ и смотри на врага.", category);
        loadString("subtitle.stage7.block_hold", "§c[Обучение] Возьмите железный меч в руку и удерживайте ПКМ для блока.", category);
        loadString("subtitle.stage7.block_hold2", "§c[Обучение] Возьмите молот в руку и удерживайте ПКМ для блока.", category);
        loadString("subtitle.stage7.block_hold3", "§c[Обучение] Возьмите щит во вторую (левую) руку и удерживайте ПКМ для блока.", category);
        // Stage 7 – shield craft and dodge
        loadString("subtitle.stage7.shield_craft", "§e[Боцман] Возьми щиты. Объедини копьё и пику со щитами в крафте (инвентарь).", category);
        loadString("subtitle.stage7.dodge_info", "§e[Боцман] Отскок x3: A/S/D + ПКМ + Пробел. Возьми любое оружие, кроме копья/пики со щитом!", category);
        loadString("subtitle.stage7.dodge_progress", "§e[Боцман] Отскок %d/3 — продолжай!", category);
        loadString("subtitle.stage7.done", "§a[Боцман] Превосходно! Ты освоил боевую систему. Впереди — настоящий бой!", category);
        loadString("subtitle.stage7.block_success", "§a[Боцман] Удар заблокирован! Продолжаем.", category);

        // Stage 8
        loadString("subtitle.stage8.start", "§c[Капитан] Бунт на корабле! Убейте всех (5) бунтовщиков! (0/5)", category);
        loadString("subtitle.stage8.progress", "§c[Капитан] Убито: %d/5", category);
        loadString("subtitle.stage8.done", "§a[Капитан] Отличная работа! Подойди ко мне (ПКМ), чтобы закончить.", category);
        loadString("subtitle.stage8.end", "§aОтлично! Вы научились основам боя.", category);

        // Stage 9
        loadString("subtitle.stage9.captain1", "§e[Капитан] Для постройки корабля нам нужно 64 доски. Подойди к точке сбора ресурсов (Блок с крестом), открой его и выбери доски!", category);
        loadString("subtitle.stage9.captain2", "§e[Капитан] Ресурсы нужно правильно продать тем, кто в них нуждается. Нажми на блок и забери ресурсы!", category);
        loadString("subtitle.stage9.captain3", "§e[Капитан] Сейчас я тебя познакомлю с важным человеком, следуй за мной!", category);

        // Stage 10
        loadString("subtitle.stage10.jaqen", "§4[Якен Хгар] Вижу, ты уже готов выйти во внешний мир?", category);
        loadString("subtitle.stage10.jaqen1", "§4Человек имеет много лиц.", category);

        // Other messages
        loadString("chat.tutorial_complete", "§a[Обучение] Поздравляем! Обучение успешно пройдено.", category);
        loadString("chat.commands_blocked", "§c[Обучение] Команды недоступны во время обучения.", category);
        
        loadString("overlay.open_menu_prompt", "§e[Обучение] Откройте главное меню (По умолчанию L).", category);
        
        // Entity names
        loadString("entity.captain", "Капитан", category);
        loadString("entity.drunkard", "Пьяница", category);
        loadString("entity.dummy", "Манекен", category);
        loadString("entity.boatswain", "Боцман", category);
        loadString("entity.sailor", "Матрос", category);
        loadString("entity.mutineer", "Бунтовщик", category);
        loadString("entity.bartender", "Трактирщик", category);
        loadString("entity.jaqen", "Якен Хгар", category);
        loadString("entity.armorsmith", "Бронник", category);
        loadString("entity.mwsmith", "Оружейник", category);

        // --- Substeps Stage 3 (Factions Menu) ---
        loadString("substep.3.0", "Капитан: Это вкладка Достижений. Здесь отслеживается ваш прогресс.", category);
        loadString("substep.3.1", "Капитан: Теперь откройте Карту.", category);
        loadString("substep.3.2", "Капитан: На карте есть путевые точки. Приблизьте карту (колесиком мыши), чтобы рассмотреть их.", category);
        loadString("substep.3.3", "Капитан: В правом верхнем углу — кнопка создания своей путевой точки. Теперь закройте карту (ESC/E).", category);
        loadString("substep.3.4", "Капитан: Теперь откройте Фракции.", category);
        loadString("substep.3.5", "Капитан: Нажмите 'Другие фракции'.", category);
        loadString("substep.3.6", "Капитан: Выберите фракцию Тортуга.", category);
        loadString("substep.3.7", "Капитан: Здесь информация о фракции: лидер, помощник, столица.", category);
        loadString("substep.3.8", "Капитан: Это шкала вашей репутации у фракции.", category);
        loadString("substep.3.9", "Капитан: Нажмите кнопку подачи заявки.", category);
        loadString("substep.3.10", "Капитан: Справа — кнопки: Участники, Казна, Дом фракции, Телепорт.", category);
        loadString("substep.3.11", "Капитан: Откройте список участников.", category);
        loadString("substep.3.12", "Капитан: Нажмите 'Управление титулами'.", category);
        loadString("substep.3.13", "Капитан: Создайте титул с любым названием и правами.", category);
        loadString("substep.3.14", "Капитан: Сохраните титул.", category);
        loadString("substep.3.15", "Капитан: Откройте иерархию титулов.", category);
        loadString("substep.3.16", "Капитан: Подвиньте ваш титул выше или ниже.", category);
        loadString("substep.3.17", "Капитан: Сохраните и вернитесь к участникам.", category);
        loadString("substep.3.18", "Капитан: Нажмите на свой ник в списке.", category);
        loadString("substep.3.19", "Капитан: Выберите 'Установить титул'.", category);
        loadString("substep.3.20", "Капитан: Присвойте себе созданный титул.", category);
        loadString("substep.3.21", "Капитан: Вернитесь на главную страницу фракции.", category);
        loadString("substep.3.22", "Капитан: Перейдите в Казну. Я выдал вам 100 монет.", category);
        loadString("substep.3.23", "Капитан: Создайте новый сбор.", category);
        loadString("substep.3.24", "Капитан: Введите название, сумму и сохраните.", category);
        loadString("substep.3.25", "Капитан: Нажмите на созданный сбор.", category);
        loadString("substep.3.26", "Капитан: Нажмите 'Пополнить счет'.", category);
        loadString("substep.3.27", "Капитан: Введите сумму и подтвердите.", category);
        loadString("substep.3.28", "Капитан: Здесь отображается история всех транзакций казны.", category);
        loadString("substep.3.29", "Капитан: Кнопки 'Дом фракции' и 'Телепорт' — так вы устанавливаете и посещаете базу фракции.", category);
        loadString("substep.3.30", "Капитан: Это кнопка Дипломатии. Она открывает интерактивную карту мира с точками фракций, территориями и крепостями. Входить не нужно.", category);
        loadString("substep.3.31", "Капитан: А это — Военный совет. Нажмите на него, чтобы войти.", category);
        loadString("substep.3.32", "Капитан: Здесь можно объявлять войны, заключать союзы и мирные договоры с другими фракциями. Ознакомься с кнопками.", category);
        loadString("substep.3.33", "Капитан: Отлично, вернитесь назад.", category);
        loadString("substep.3.34", "Капитан: Отлично! Фракции освоены. Обучение продолжается...", category);
        loadString("substep.3.35", "Капитан: Отлично! Фракции освоены. Обучение продолжается...", category);
        loadString("substep.3.36", "Капитан: Это вкладка Языков. Пока пропустим.", category);
        loadString("substep.3.37", "Капитан: Откройте Братства (отряды).", category);
        loadString("substep.3.38", "Капитан: Нажмите 'Создать отряд'.", category);
        loadString("substep.3.39", "Капитан: Введите название и сохраните.", category);
        loadString("substep.3.40", "Капитан: Нажмите на созданный отряд.", category);
        loadString("substep.3.41", "Капитан: Нажмите 'Пригласить игрока'.", category);
        loadString("substep.3.42", "Капитан: Введите 'Капитан' и отправьте.", category);
        loadString("substep.3.43", "Капитан: Это вкладка Титулов. Здесь вы видите все доступные титулы.", category);
        loadString("substep.3.44", "Капитан: Это Атрибуты. Здесь настраиваются ваши характеристики.", category);
        loadString("substep.3.45", "Капитан: И настройки. Вы прошли обучение интерфейсам!", category);

        // --- Substeps Stage 4 (Banner) ---
        loadString("substep.4.0", "Интендант: Это приват — он защищает территорию от гриферов. Поставьте выданный блок на землю, а НА НЕГО поставьте Знамя.", category);
        loadString("substep.4.1", "Интендант: Отлично! Знамя стоит! Кликните ПКМ по знамени, чтобы открыть его настройки.", category);
        loadString("substep.4.2", "Интендант: В настройках знамени включите вайт-лист (именная защита) и впишите братство в формате f/Название (например, f/Отряд).", category);
        loadString("substep.4.3", "Интендант: Вы включили вайт-лист, но забыли добавить братство! Откройте настройки знамени снова и впишите f/Название.", category);
        loadString("substep.4.4", "Интендант: Готово! Теперь сломайте знамя (ЛКМ по нему), чтобы снять приват.", category);
        loadString("substep.4.5", "Интендант: Приват освоен! Переходим к следующему этапу...", category);

        // --- Substeps Stage 6 (Blacksmith) ---
        loadString("substep.6.0", "§e[Бронник] Откройте интерфейс кузнеца-бронника, нажав ПКМ по нему.", category);
        loadString("substep.6.1", "§e[Бронник] Положите бронзовый нагрудник в центральный слот.", category);
        loadString("substep.6.2", "§e[Бронник] Выберите любой чар из списка.", category);
        loadString("substep.6.3", "§e[Бронник] Нажмите кнопку Перековать внизу.", category);
        loadString("substep.6.4", "§e[Обучение] Теперь подойдите к наковальне и откройте её интерфейс (ПКМ).", category);
        loadString("substep.6.5", "§e[Обучение] Положите меч в левый слот.", category);
        loadString("substep.6.6", "§e[Обучение] Положите чертёж в верхний слот.", category);
        loadString("substep.6.7", "§e[Обучение] Заберите улучшенный меч.", category);

        // GUI Hints
        loadString("gui.map.regions_info", "Информация о регионах", category);
        loadString("gui.factions.tortuga", "Тортуга", category);
        loadString("gui.factions.scroll_down", "Прокрутите вниз", category);
        loadString("gui.factions.rmb_nickname", "ПКМ", category);
        loadString("gui.factions.press_esc_close", "Нажмите ESC чтобы закрыть", category);
        loadString("gui.banner.switch_mode", "ЛКМ - переключить режим", category);
        loadString("gui.banner.press_esc", "Нажмите ESC для выхода", category);

        loadString("hint.banner.toggle", "Сначала нажмите эту кнопку, чтобы переключить режим на вайт-лист.", category);
        loadString("hint.banner.close", "Отлично! Теперь нажмите ESC, чтобы сохранить настройки и закрыть окно.", category);

        loadString("hint.faction.tortuga_append", " (Тортуга)", category);
        loadString("hint.faction.scroll_right", " (Используйте колесико мыши для прокрутки вправо до Тортуги)", category);
        loadString("hint.faction.hierarchy", "Нажмите 'Иерархия титулов' для продолжения", category);
        loadString("hint.faction.rmb_nick", " (ПКМ по своему нику)", category);
        loadString("hint.faction.scroll_down", " (Используйте колесико мыши для прокрутки вниз)", category);
        loadString("hint.faction.esc_close", " (Нажмите ESC чтобы закрыть окно)", category);
        loadString("hint.faction.rmb_capital", " (ПКМ по любой столице - цветной точке)", category);
        loadString("hint.faction.lmb_continue", " (Нажмите ЛКМ, чтобы закрыть меню и продолжить)", category);
        loadString("hint.faction.fortress1", "Капитан: А это крепость. Вы можете захватить её. (Кликните ЛКМ для продолжения)", category);
        loadString("hint.faction.fortress2", " (Кликните ЛКМ для продолжения)", category);
        
        loadString("hint.map.regions", " (Также на карте обозначены регионы, биомы и моря. Кликните ЛКМ, чтобы продолжить)", category);

        if (config.hasChanged()) {
            config.save();
        }
    }

    private static void loadString(String key, String defaultValue, String category) {
        String value = config.get(category, key, defaultValue).getString();
        texts.put(key, value);
    }

    public static Map<String, String> getTexts() {
        return texts;
    }

    public static String get(String key) {
        return texts.getOrDefault(key, key);
    }
}
