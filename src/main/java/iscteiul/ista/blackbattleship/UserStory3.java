package iscteiul.ista.blackbattleship;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Page Object Class da user story US3 – Jogar contra o robot.
 * <p>
 * Concentra as operações sobre a página do jogo de batalha naval
 * (papergames.io) que usam localizadores CSS/XPath e que são necessárias para
 * iniciar uma partida contra o robot <em>Paper Man</em> e observar o ecrã de
 * jogo. A classe de teste {@code UserStory3Test} só chama os métodos desta
 * classe e valida os resultados.
 * </p>
 */
public class UserStory3 {

    /** Endereço da página do jogo. */
    public static final String URL = "https://papergames.io/en/battleship";

    /** Tempo máximo de espera por um elemento. */
    private static final Duration TIMEOUT = Duration.ofSeconds(15);

    /** Pequena pausa entre ações, para a execução se parecer com a de uma pessoa. */
    private static final long HUMAN_PAUSE_MS = 700;

    // ---------------------------------------------------------------------
    // Localizadores
    // ---------------------------------------------------------------------

    /** Botão "Consent" da janela de consentimento de cookies. */
    private static final By COOKIE_CONSENT_BUTTON = By.cssSelector("button.fc-cta-consent");

    /** Botão "Play vs robot". */
    private static final By PLAY_VS_ROBOT_BUTTON =
            By.xpath("//button[contains(normalize-space(.), 'Play vs robot')]");

    /** Campo de texto "Nickname" da janela "Who are you?". */
    private static final By NICKNAME_INPUT =
            By.cssSelector("app-guest-registration-dialog input[type='text']");

    /** Botão "Continue" da janela do nickname. */
    private static final By CONTINUE_BUTTON =
            By.cssSelector("app-guest-registration-dialog button[type='submit']");

    /** Nome do robot, mostrado no topo do ecrã de jogo. */
    private static final By ROBOT_NAME = textLocator("Paper Man");

    /** Indicação "Attack your opponent!" por cima da grelha do adversário. */
    private static final By ATTACK_PROMPT = textLocator("Attack your opponent!");

    /**
     * Relógios dos jogadores. Cada relógio é um componente {@code app-timer} cujo texto
     * está partido em três elementos (minutos, ":" e segundos).
     */
    private static final By CLOCK = By.cssSelector("app-timer");

    /** Botão "Abort game" (só existe enquanto ainda não houve disparos). */
    private static final By ABORT_GAME_BUTTON =
            By.xpath("//button[contains(normalize-space(.), 'Abort game')]");

    /** Botão "Resign" (passa a existir depois do primeiro disparo). */
    private static final By RESIGN_BUTTON =
            By.xpath("//button[contains(normalize-space(.), 'Resign')]");

    private final WebDriver driver;
    private final WebDriverWait wait;

    /**
     * Cria o Page Object.
     *
     * @param driver o WebDriver já iniciado
     */
    public UserStory3(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TIMEOUT);
    }

    // ---------------------------------------------------------------------
    // Ações
    // ---------------------------------------------------------------------

    /** Abre a página do jogo. */
    public void open() {
        driver.get(URL);
    }

    /**
     * Aceita o consentimento de cookies, se a janela aparecer.
     * Se não aparecer dentro do tempo limite, segue em frente sem falhar.
     */
    public void acceptCookiesIfPresent() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(COOKIE_CONSENT_BUTTON))
                    .click();
            pause();
        } catch (TimeoutException e) {
            // A janela de cookies não apareceu; não é necessário fechá-la.
        }
    }

    /**
     * Clica no botão "Play vs robot" e espera que a janela do nickname abra.
     * Como a página por vezes ainda não está pronta no primeiro clique (e o clique
     * perde-se), repete o clique até 3 vezes se a janela não aparecer.
     */
    public void clickPlayVsRobot() {
        for (int attempt = 1; attempt <= 3; attempt++) {
            click(PLAY_VS_ROBOT_BUTTON);
            try {
                new WebDriverWait(driver, Duration.ofSeconds(6))
                        .until(ExpectedConditions.visibilityOfElementLocated(NICKNAME_INPUT));
                return;
            } catch (TimeoutException e) {
                // A janela não abriu; tenta outra vez.
            }
        }
    }

    /**
     * Escreve o nickname no campo "Nickname".
     *
     * @param nickname o nickname a escrever
     */
    public void typeNickname(String nickname) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(NICKNAME_INPUT));
        input.clear();
        input.sendKeys(nickname);
        pause();
    }

    /** Clica no botão "Continue" da janela do nickname. */
    public void clickContinue() {
        click(CONTINUE_BUTTON);
    }

    /**
     * Escolhe o modo contra o robot, escreve o nickname e confirma,
     * começando uma partida como convidado.
     *
     * @param nickname o nickname a usar
     */
    public void startRobotGameAsGuest(String nickname) {
        clickPlayVsRobot();
        typeNickname(nickname);
        clickContinue();
    }

    /**
     * Dispara sobre uma casa da grelha do adversário (a segunda grelha da página).
     *
     * @param row    linha da casa, de 0 a 9
     * @param column coluna da casa, de 0 a 9
     */
    public void fireAtCell(int row, int column) {
        click(opponentCell(row, column));
    }

    // ---------------------------------------------------------------------
    // Consultas (usadas nas asserções dos testes)
    // ---------------------------------------------------------------------

    /** @return {@code true} se o botão "Play vs robot" está visível */
    public boolean isPlayVsRobotButtonDisplayed() {
        return isAnyVisible(PLAY_VS_ROBOT_BUTTON);
    }

    /**
     * Espera que o ecrã de jogo contra o robot abra.
     *
     * @param timeout tempo máximo de espera
     * @return {@code true} se o endereço passou a conter {@code /en/r/} dentro do tempo limite
     */
    public boolean waitForGameScreen(Duration timeout) {
        try {
            return new WebDriverWait(driver, timeout).until(ExpectedConditions.urlContains("/en/r/"));
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** @return o endereço atual do browser */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Verifica se o nickname aparece no ecrã de jogo.
     *
     * @param nickname o nickname escolhido (sem aspas)
     * @return {@code true} se algum elemento visível mostra esse texto
     */
    public boolean isNicknameShown(String nickname) {
        return isAnyVisible(textLocator(nickname));
    }

    /** @return {@code true} se o nome do robot (Paper Man) está visível */
    public boolean isRobotNameDisplayed() {
        return isAnyVisible(ROBOT_NAME);
    }

    /**
     * Lê os relógios dos jogadores mostrados no topo do ecrã de jogo.
     * Espera até que existam pelo menos dois relógios visíveis.
     *
     * @return o tempo de cada relógio, em segundos
     */
    public List<Integer> getClockSeconds() {
        try {
            new WebDriverWait(driver, TIMEOUT)
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> readClocks().size() >= 2);
        } catch (TimeoutException e) {
            // Devolve o que existir; o teste decide se é suficiente.
        }
        return readClocks();
    }

    /** @return {@code true} se a indicação "Attack your opponent!" está visível */
    public boolean isAttackYourOpponentDisplayed() {
        return isAnyVisible(ATTACK_PROMPT);
    }

    /** @return {@code true} se o botão "Abort game" está visível */
    public boolean isAbortGameButtonDisplayed() {
        return isAnyVisible(ABORT_GAME_BUTTON);
    }

    /** @return {@code true} se o botão "Resign" passa a estar visível (espera até ao tempo limite) */
    public boolean isResignButtonDisplayed() {
        return isAnyVisible(RESIGN_BUTTON);
    }

    /** @return {@code true} se o botão "Resign" está visível neste momento (sem esperar) */
    public boolean isResignButtonDisplayedNow() {
        return driver.findElements(RESIGN_BUTTON).stream().anyMatch(WebElement::isDisplayed);
    }

    // ---------------------------------------------------------------------
    // Auxiliares
    // ---------------------------------------------------------------------

    /** Localizador de qualquer elemento (exceto script e style) que contenha o texto dado. */
    private static By textLocator(String text) {
        return By.xpath(String.format(
                "//*[not(self::script) and not(self::style) and text()[contains(., '%s')]]", text));
    }

    /** Localizador de uma casa da grelha do adversário (a segunda tabela do tabuleiro). */
    private static By opponentCell(int row, int column) {
        return By.xpath(String.format(
                "(//table[contains(concat(' ', normalize-space(@class), ' '), ' table-board ')])[2]"
                        + "//td[contains(concat(' ', normalize-space(@class), ' '), ' cell-%d-%d ')]",
                row, column));
    }

    /** Lê, neste momento, os relógios visíveis (textos no formato mm:ss), em segundos. */
    private List<Integer> readClocks() {
        List<Integer> seconds = new ArrayList<>();
        for (WebElement element : driver.findElements(CLOCK)) {
            try {
                String text = element.getText().replaceAll("\\s", "");
                if (element.isDisplayed() && text.matches("\\d{2}:\\d{2}")) {
                    seconds.add(Integer.parseInt(text.substring(0, 2)) * 60
                            + Integer.parseInt(text.substring(3)));
                }
            } catch (StaleElementReferenceException e) {
                // O elemento foi substituído enquanto lia; ignora e segue.
            }
        }
        return seconds;
    }

    /**
     * Espera que pelo menos um dos elementos encontrados fique visível.
     * Útil quando a página tem várias cópias do mesmo texto e algumas estão escondidas.
     */
    private boolean isAnyVisible(By locator) {
        try {
            return new WebDriverWait(driver, TIMEOUT)
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> d.findElements(locator).stream().anyMatch(WebElement::isDisplayed));
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Clica no elemento quando estiver clicável. Se um anúncio ou outro
     * elemento tapar o clique, repete o clique por JavaScript.
     */
    private void click(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        try {
            element.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
        pause();
    }

    /** Pausa curta entre ações. */
    private void pause() {
        try {
            Thread.sleep(HUMAN_PAUSE_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}