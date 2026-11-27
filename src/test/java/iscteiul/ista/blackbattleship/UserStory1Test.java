package iscteiul.ista.blackbattleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Page Test Class da user story US1 – Jogar como convidado.
 * <p>
 * Testes de aceitação (caixa-preta) derivados do cenário {@code UserStoryTest1}
 * do ficheiro {@code TestSuite_129873.side}. Cada teste corresponde a um
 * critério de aceitação do Product Backlog e começa num browser limpo, por isso
 * a janela de cookies e a janela do nickname aparecem sempre.
 * </p>
 */
class UserStory1Test {

    /** Nickname usado nos testes. */
    private static final String NICKNAME = "TesteMartim";

    private WebDriver driver;
    private UserStory1 page;

    /** Abre um browser novo, a página do jogo e fecha a janela de cookies. */
    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        page = new UserStory1(driver);
        page.open();
        page.acceptCookiesIfPresent();
    }

    /** Fecha o browser no fim de cada teste. */
    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * CA1.1 – Dado que não tenho sessão iniciada, quando abro a página do jogo,
     * então os botões Play with a friend, Play vs robot e Play online estão visíveis.
     */
    @Test
    @DisplayName("CA1.1 – Os três modos de jogo estão visíveis sem sessão iniciada")
    void ca1_1_modosDeJogoVisiveis() {
        assertTrue(page.isPlayWithFriendButtonDisplayed(), "Play with a friend deve estar visível");
        assertTrue(page.isPlayVsRobotButtonDisplayed(), "Play vs robot deve estar visível");
        assertTrue(page.isPlayOnlineButtonDisplayed(), "Play online deve estar visível");
    }

    /**
     * CA1.2 – Dado que não tenho conta, quando escolho um modo de jogo,
     * então só me é pedido um nickname, sem formulário de registo nem de login.
     */
    @Test
    @DisplayName("CA1.2 – Só é pedido um nickname, sem registo nem login")
    void ca1_2_soPedeNickname() {
        page.clickPlayVsRobot();

        assertTrue(page.isNicknameDialogDisplayed(), "A janela Who are you? deve abrir");
        assertEquals(1, page.countNicknameFields(), "Só deve existir o campo Nickname");
        assertFalse(page.hasLoginOrRegistrationFields(),
                "Não deve haver campos de palavra-passe nem de email");
    }

    /**
     * CA1.3 – Dado que estou a jogar como convidado, quando a partida começa,
     * então o meu nickname aparece no topo do ecrã de jogo.
     */
    @Test
    @DisplayName("CA1.3 – O nickname aparece no ecrã de jogo")
    void ca1_3_nicknameApareceNoJogo() {
        page.startRobotGameAsGuest(NICKNAME);

        assertTrue(page.waitForGameScreen(), "O ecrã de jogo (/en/r/) deve abrir");
        assertTrue(page.isNicknameShownInGame(NICKNAME), "O nickname deve aparecer no ecrã de jogo");
    }
}
