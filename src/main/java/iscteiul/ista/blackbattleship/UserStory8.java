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
import java.util.List;

/**
 * Page Object Class da user story US8 – Disparar sobre o adversário.
 * <p>
 * Concentra as operações sobre o ecrã de jogo contra o robot (papergames.io)
 * necessárias para disparar sobre as casas das grelhas e observar o resultado.
 * Cada grelha é uma tabela {@code table.table-board} (índice 0: a do jogador;
 * índice 1: a do adversário) e cada casa é um {@code td.cell-<linha>-<coluna>}.
 * Uma casa já atacada passa a conter um marcador {@code .hit} (acerto) ou
 * {@code .no-hit} (falhanço). A classe de teste {@code UserStory8Test} só chama
 * os métodos desta classe e valida os resultados.
 * </p>
 */
public class UserStory8 {

    /** Endereço da página do jogo. */
    public static final String URL = "https://papergames.io/en/battleship";

    /** Índice da grelha do jogador. */
    public static final int MY_BOARD = 0;

    /** Índice da grelha do adversário. */
    public static final int OPPONENT_BOARD = 1;

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

    /** Tabelas das grelhas: a primeira é a do jogador e a segunda a do adversário. */
    private static final By BOARD_TABLE = By.cssSelector("table.table-board");

    /** Casas de uma grelha. */
    private static final By CELL = By.cssSelector("td");

    /** Marcadores de disparo dentro de uma grelha: acerto ({@code .hit}) ou falhanço ({@code .no-hit}). */
    private static final By SHOT_MARKER = By.cssSelector(".hit, .no-hit");

    /** Indicação "Attack your opponent!", visível quando é a vez do jogador. */
    private static final By ATTACK_PROMPT = textLocator("Attack your opponent!");

    private final WebDriver driver;
    private final WebDriverWait wait;

    /**
     * Cria o Page Object.
     *
     * @param driver o WebDriver já iniciado
     */
    public UserStory8(WebDriver driver) {
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
     * Dispara sobre uma casa da grelha do adversário e faz uma pausa curta.
     *
     * @param row    linha da casa, de 0 a 9
     * @param column coluna da casa, de 0 a 9
     */
    public void fireAtOpponentCell(int row, int column) {
        click(cell(OPPONENT_BOARD, row, column));
    }

    /**
     * Dispara sobre uma casa da grelha do adversário sem pausa depois do clique,
     * para o teste poder observar a página logo a seguir ao disparo.
     *
     * @param row    linha da casa, de 0 a 9
     * @param column coluna da casa, de 0 a 9
     */
    public void fireAtOpponentCellQuickly(int row, int column) {
        clickWithoutPause(cell(OPPONENT_BOARD, row, column));
    }

    /**
     * Clica numa casa da grelha do próprio jogador (onde não se pode disparar).
     *
     * @param row    linha da casa, de 0 a 9
     * @param column coluna da casa, de 0 a 9
     */
    public void clickMyCell(int row, int column) {
        click(cell(MY_BOARD, row, column));
    }

    /**
     * Espera, sem fazer nada, durante o tempo indicado. Serve para dar tempo ao
     * site de reagir a um clique que, a funcionar bem, não deve ter efeito.
     *
     * @param millis duração da espera em milissegundos
     */
    public void waitMillis(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // ---------------------------------------------------------------------
    // Consultas (usadas nas asserções dos testes)
    // ---------------------------------------------------------------------

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

    /**
     * Espera que as duas grelhas estejam desenhadas, cada uma com as 100 casas.
     *
     * @return {@code true} se as duas grelhas ficaram completas dentro do tempo limite
     */
    public boolean waitForBoards() {
        try {
            return new WebDriverWait(driver, TIMEOUT)
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> {
                        List<WebElement> boards = d.findElements(BOARD_TABLE);
                        return boards.size() >= 2
                                && boards.get(MY_BOARD).findElements(CELL).size() >= 100
                                && boards.get(OPPONENT_BOARD).findElements(CELL).size() >= 100;
                    });
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** @return {@code true} se a indicação "Attack your opponent!" está visível neste momento (sem esperar) */
    public boolean isMyTurnNow() {
        return driver.findElements(ATTACK_PROMPT).stream().anyMatch(WebElement::isDisplayed);
    }

    /**
     * Conta os marcadores de disparo (acertos e falhanços) numa grelha.
     *
     * @param board {@link #MY_BOARD} ou {@link #OPPONENT_BOARD}
     * @return o número de marcadores visíveis
     */
    public int countShotMarkers(int board) {
        try {
            return board(board).findElements(SHOT_MARKER).size();
        } catch (StaleElementReferenceException e) {
            return board(board).findElements(SHOT_MARKER).size();
        }
    }

    /**
     * Espera que a grelha tenha pelo menos o número de marcadores indicado.
     *
     * @param board   {@link #MY_BOARD} ou {@link #OPPONENT_BOARD}
     * @param minimum número mínimo de marcadores
     * @param timeout tempo máximo de espera
     * @return {@code true} se o número foi atingido dentro do tempo limite
     */
    public boolean waitForShotMarkers(int board, int minimum, Duration timeout) {
        try {
            return new WebDriverWait(driver, timeout)
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> countShotMarkers(board) >= minimum);
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Indica se uma casa já foi atacada, isto é, se tem um marcador de acerto ou de falhanço.
     *
     * @param board  {@link #MY_BOARD} ou {@link #OPPONENT_BOARD}
     * @param row    linha da casa, de 0 a 9
     * @param column coluna da casa, de 0 a 9
     * @return {@code true} se a casa tem um marcador de disparo
     */
    public boolean isCellMarked(int board, int row, int column) {
        return !driver.findElements(cell(board, row, column)).isEmpty()
                && !driver.findElement(cell(board, row, column)).findElements(SHOT_MARKER).isEmpty();
    }

    /**
     * Devolve o HTML de uma casa, para mostrar nas mensagens de falha dos testes.
     *
     * @param board  {@link #MY_BOARD} ou {@link #OPPONENT_BOARD}
     * @param row    linha da casa, de 0 a 9
     * @param column coluna da casa, de 0 a 9
     * @return o HTML da casa
     */
    public String getCellHtml(int board, int row, int column) {
        return driver.findElement(cell(board, row, column)).getAttribute("outerHTML");
    }

    // ---------------------------------------------------------------------
    // Auxiliares
    // ---------------------------------------------------------------------

    /** Localizador de qualquer elemento (exceto script e style) que contenha o texto dado. */
    private static By textLocator(String text) {
        return By.xpath(String.format(
                "//*[not(self::script) and not(self::style) and text()[contains(., '%s')]]", text));
    }

    /** Localizador de uma casa de uma das grelhas (0: do jogador; 1: do adversário). */
    private static By cell(int board, int row, int column) {
        return By.xpath(String.format(
                "(//table[contains(concat(' ', normalize-space(@class), ' '), ' table-board ')])[%d]"
                        + "//td[contains(concat(' ', normalize-space(@class), ' '), ' cell-%d-%d ')]",
                board + 1, row, column));
    }

    /** @return a tabela da grelha pedida */
    private WebElement board(int board) {
        return driver.findElements(BOARD_TABLE).get(board);
    }

    /**
     * Clica no elemento quando estiver clicável. Se um anúncio ou outro
     * elemento tapar o clique, repete o clique por JavaScript.
     */
    private void click(By locator) {
        clickWithoutPause(locator);
        pause();
    }

    /** Como {@link #click(By)}, mas sem a pausa final. */
    private void clickWithoutPause(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        try {
            element.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
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

    /** Pausa curta entre ações. */
    private void pause() {
        waitMillis(HUMAN_PAUSE_MS);
    }
}