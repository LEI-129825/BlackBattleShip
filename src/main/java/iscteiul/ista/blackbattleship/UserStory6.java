package iscteiul.ista.blackbattleship;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Page Object Class da user story US6 – Ver o tabuleiro e a frota.
 * <p>
 * Concentra as operações sobre o ecrã de jogo contra o robot (papergames.io)
 * que permitem observar as duas grelhas: a do jogador (índice 0, com o título
 * <em>Your boats</em>) e a do adversário (índice 1). Cada grelha é uma tabela
 * {@code table.table-board}; cada casa é um {@code td.cell-<linha>-<coluna>} e
 * as casas com navio contêm um {@code div.ship-cell}. A classe de teste
 * {@code UserStory6Test} só chama os métodos desta classe e valida os resultados.
 * </p>
 */
public class UserStory6 {

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

    /** Linhas de uma grelha. */
    private static final By ROW = By.cssSelector("tr");

    /** Casas de uma grelha. */
    private static final By CELL = By.cssSelector("td");

    /** Elemento desenhado dentro de uma casa que contém um navio. */
    private static final By SHIP_CELL = By.cssSelector("div.ship-cell");

    /** Casas (td) que contêm um navio. */
    private static final By CELL_WITH_SHIP = By.xpath(
            ".//td[.//div[contains(concat(' ', normalize-space(@class), ' '), ' ship-cell ')]]");

    /** Título "Your boats" por cima da grelha do jogador. */
    private static final By YOUR_BOATS_TITLE = textLocator("Your boats");

    private final WebDriver driver;
    private final WebDriverWait wait;

    /**
     * Cria o Page Object.
     *
     * @param driver o WebDriver já iniciado
     */
    public UserStory6(WebDriver driver) {
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

    // ---------------------------------------------------------------------
    // Esperas
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
     * Espera que as duas grelhas estejam desenhadas (com 100 casas cada) e que os
     * navios do jogador já tenham sido colocados.
     *
     * @return {@code true} se as grelhas ficaram prontas dentro do tempo limite
     */
    public boolean waitForBoards() {
        try {
            return new WebDriverWait(driver, TIMEOUT)
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> {
                        List<WebElement> boards = d.findElements(BOARD_TABLE);
                        return boards.size() >= 2
                                && boards.get(MY_BOARD).findElements(CELL).size() >= 100
                                && boards.get(OPPONENT_BOARD).findElements(CELL).size() >= 100
                                && !boards.get(MY_BOARD).findElements(SHIP_CELL).isEmpty();
                    });
        } catch (TimeoutException e) {
            return false;
        }
    }

    // ---------------------------------------------------------------------
    // Consultas (usadas nas asserções dos testes)
    // ---------------------------------------------------------------------

    /** @return {@code true} se o título "Your boats" está visível */
    public boolean isYourBoatsTitleDisplayed() {
        try {
            return new WebDriverWait(driver, TIMEOUT)
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> d.findElements(YOUR_BOATS_TITLE).stream().anyMatch(WebElement::isDisplayed));
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** @return o número de grelhas (tabelas) encontradas na página */
    public int getBoardCount() {
        return driver.findElements(BOARD_TABLE).size();
    }

    /**
     * Indica se as duas grelhas aparecem lado a lado: a do jogador à esquerda da do
     * adversário e ao mesmo nível vertical.
     *
     * @return {@code true} se estiverem lado a lado
     */
    public boolean areBoardsSideBySide() {
        List<WebElement> boards = driver.findElements(BOARD_TABLE);
        if (boards.size() < 2) {
            return false;
        }
        Rectangle mine = boards.get(MY_BOARD).getRect();
        Rectangle opponent = boards.get(OPPONENT_BOARD).getRect();
        boolean mineIsOnTheLeft = mine.getX() + mine.getWidth() <= opponent.getX();
        boolean sameLevel = Math.abs(mine.getY() - opponent.getY()) <= 20;
        return mineIsOnTheLeft && sameLevel;
    }

    /**
     * @param board índice da grelha ({@link #MY_BOARD} ou {@link #OPPONENT_BOARD})
     * @return o número de casas de cada linha da grelha, por ordem
     */
    public List<Integer> getCellsPerRow(int board) {
        List<Integer> cellsPerRow = new ArrayList<>();
        for (WebElement row : getBoard(board).findElements(ROW)) {
            cellsPerRow.add(row.findElements(CELL).size());
        }
        return cellsPerRow;
    }

    /**
     * @param board índice da grelha
     * @return o número total de casas da grelha
     */
    public int countCells(int board) {
        return getBoard(board).findElements(CELL).size();
    }

    /**
     * @param board índice da grelha
     * @return o número de casas da grelha que mostram um navio
     */
    public int countShipCells(int board) {
        return getBoard(board).findElements(SHIP_CELL).size();
    }

    /**
     * Agrupa as casas com navio pelas classes CSS próprias de cada navio (as que o
     * site acrescenta além de {@code cell-<linha>-<coluna>}), para contar os navios.
     *
     * @param board índice da grelha
     * @return o número de casas de cada grupo de navio
     */
    public Map<String, Integer> getShipGroups(int board) {
        Map<String, Integer> groups = new TreeMap<>();
        for (WebElement cell : getBoard(board).findElements(CELL_WITH_SHIP)) {
            List<String> tokens = new ArrayList<>();
            for (String token : cell.getAttribute("class").trim().split("\\s+")) {
                if (!token.startsWith("cell-") && !token.equals("ng-star-inserted")) {
                    tokens.add(token);
                }
            }
            tokens.sort(String::compareTo);
            String group = tokens.isEmpty() ? "?" : String.join("+", tokens);
            groups.merge(group, 1, Integer::sum);
        }
        return groups;
    }

    // ---------------------------------------------------------------------
    // Auxiliares
    // ---------------------------------------------------------------------

    /** Localizador de qualquer elemento (exceto script e style) que contenha o texto dado. */
    private static By textLocator(String text) {
        return By.xpath(String.format(
                "//*[not(self::script) and not(self::style) and text()[contains(., '%s')]]", text));
    }

    /** Devolve a tabela da grelha pedida. */
    private WebElement getBoard(int board) {
        return driver.findElements(BOARD_TABLE).get(board);
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