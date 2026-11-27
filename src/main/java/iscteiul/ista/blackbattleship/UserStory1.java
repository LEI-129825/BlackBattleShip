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

/**
 * Page Object Class da user story US1 – Jogar como convidado.
 * <p>
 * Concentra todas as operações sobre a página do jogo de batalha naval
 * (papergames.io) que usam localizadores CSS/XPath. A classe de teste
 * {@code UserStory1Test} limita-se a chamar os métodos desta classe e a
 * validar os resultados, pelo que uma alteração na página só obriga a mudar
 * os localizadores definidos aqui.
 * </p>
 */
public class UserStory1 {

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

    /** Botão "Play with a friend". */
    private static final By PLAY_WITH_FRIEND_BUTTON =
            By.xpath("//button[contains(normalize-space(.), 'Play with a friend')]");

    /** Botão "Play vs robot". */
    private static final By PLAY_VS_ROBOT_BUTTON =
            By.xpath("//button[contains(normalize-space(.), 'Play vs robot')]");

    /** Botão "Play online" (jogar online com um jogador aleatório). */
    private static final By PLAY_ONLINE_BUTTON =
            By.xpath("//button[contains(normalize-space(.), 'Play online')]");

    /** Janela "Who are you?" que pede o nickname. */
    private static final By NICKNAME_DIALOG = By.cssSelector("app-guest-registration-dialog");

    /** Campo de texto "Nickname". */
    private static final By NICKNAME_INPUT =
            By.cssSelector("app-guest-registration-dialog input[type='text']");

    /** Campos de palavra-passe ou email (formulários de login ou de registo). */
    private static final By LOGIN_OR_REGISTER_FIELDS = By.cssSelector(
            "app-guest-registration-dialog input[type='password'], "
                    + "app-guest-registration-dialog input[type='email']");

    /** Botão "Continue" da janela do nickname. */
    private static final By CONTINUE_BUTTON =
            By.cssSelector("app-guest-registration-dialog button[type='submit']");

    private final WebDriver driver;
    private final WebDriverWait wait;

    /**
     * Cria o Page Object.
     *
     * @param driver o WebDriver já iniciado
     */
    public UserStory1(WebDriver driver) {
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
     * Escreve o nickname no campo "Nickname" da janela "Who are you?".
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
    // Consultas (usadas nas asserções dos testes)
    // ---------------------------------------------------------------------

    /** @return {@code true} se o botão "Play with a friend" está visível */
    public boolean isPlayWithFriendButtonDisplayed() {
        return isVisible(PLAY_WITH_FRIEND_BUTTON);
    }

    /** @return {@code true} se o botão "Play vs robot" está visível */
    public boolean isPlayVsRobotButtonDisplayed() {
        return isVisible(PLAY_VS_ROBOT_BUTTON);
    }

    /** @return {@code true} se o botão "Play online" está visível */
    public boolean isPlayOnlineButtonDisplayed() {
        return isVisible(PLAY_ONLINE_BUTTON);
    }

    /** @return {@code true} se a janela "Who are you?" está visível */
    public boolean isNicknameDialogDisplayed() {
        return isVisible(NICKNAME_DIALOG);
    }

    /** @return o número de campos de texto (nickname) na janela "Who are you?" */
    public int countNicknameFields() {
        return driver.findElements(NICKNAME_INPUT).size();
    }

    /**
     * @return {@code true} se a janela do nickname tiver campos de palavra-passe
     *         ou de email, ou seja, um formulário de login ou de registo
     */
    public boolean hasLoginOrRegistrationFields() {
        return !driver.findElements(LOGIN_OR_REGISTER_FIELDS).isEmpty();
    }

    /**
     * Espera que o ecrã de jogo contra o robot abra.
     *
     * @return {@code true} se o endereço passou a conter {@code /en/r/}
     */
    public boolean waitForGameScreen() {
        try {
            return wait.until(ExpectedConditions.urlContains("/en/r/"));
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Verifica se o nickname aparece no ecrã de jogo.
     *
     * @param nickname o nickname escolhido (sem aspas)
     * @return {@code true} se algum elemento visível mostra exatamente esse texto
     */
    public boolean isNicknameShownInGame(String nickname) {
        By nicknameLabel = By.xpath(String.format(
                "//*[not(self::script) and not(self::style) and text()[contains(., '%s')]]",
                nickname));
        return isAnyVisible(nicknameLabel);
    }

    // ---------------------------------------------------------------------
    // Auxiliares
    // ---------------------------------------------------------------------

    /** Espera que o elemento fique visível; devolve {@code false} se não ficar. */
    private boolean isVisible(By locator) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
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