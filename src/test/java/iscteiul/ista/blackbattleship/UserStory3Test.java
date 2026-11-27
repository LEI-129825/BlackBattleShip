package iscteiul.ista.blackbattleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Page Test Class da user story US3 – Jogar contra o robot.
 * <p>
 * Testes de aceitação (caixa-preta) derivados do cenário {@code UserStoryTest3}
 * do ficheiro {@code TestSuite_129873.side}. Cada teste corresponde a um
 * critério de aceitação do Product Backlog e começa num browser limpo, por isso
 * a janela de cookies e a janela do nickname aparecem sempre.
 * </p>
 */
class UserStory3Test {

    /** Nickname usado nos testes. */
    private static final String NICKNAME = "TesteMartim";

    private WebDriver driver;
    private UserStory3 page;

    /** Abre um browser novo, a página do jogo e fecha a janela de cookies. */
    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        page = new UserStory3(driver);
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
     * CA3.1 – Dado que abro a página do jogo, então existe o botão Play vs robot.
     */
    @Test
    @DisplayName("CA3.1 – Existe o botão Play vs robot")
    void ca3_1_existeBotaoPlayVsRobot() {
        assertTrue(page.isPlayVsRobotButtonDisplayed(), "O botão Play vs robot deve estar visível");
    }

    /**
     * CA3.2 – Dado que já tenho nickname, quando clico em Play vs robot, então o ecrã
     * de jogo é mostrado em menos de 10 segundos, num endereço do tipo /en/r/&lt;código&gt;.
     */
    @Test
    @DisplayName("CA3.2 – O ecrã de jogo abre em menos de 10 s, em /en/r/<código>")
    void ca3_2_ecraDeJogoAbreRapidamente() {
        page.clickPlayVsRobot();
        page.typeNickname(NICKNAME);
        page.clickContinue();

        assertTrue(page.waitForGameScreen(Duration.ofSeconds(10)),
                "O ecrã de jogo deve abrir em menos de 10 segundos");
        assertTrue(page.getCurrentUrl().matches(".*/en/r/[^/?#]+.*"),
                "O endereço deve ser do tipo /en/r/<código>, mas é " + page.getCurrentUrl());
    }

    /**
     * CA3.3 – Dado que a partida começou, então o topo do ecrã mostra o meu nickname e o
     * robot Paper Man, cada um com um relógio que começa em 03:00. Como o relógio do jogador
     * da vez já começou a contar, aceita-se um valor próximo de 03:00 (entre 02:00 e 03:00).
     */
    @Test
    @DisplayName("CA3.3 – Topo com o meu nickname, o Paper Man e os relógios perto de 03:00")
    void ca3_3_topoComJogadoresERelogios() {
        page.startRobotGameAsGuest(NICKNAME);
        assertTrue(page.waitForGameScreen(Duration.ofSeconds(10)), "O ecrã de jogo deve abrir");

        assertTrue(page.isNicknameShown(NICKNAME), "O meu nickname deve aparecer no topo");
        assertTrue(page.isRobotNameDisplayed(), "O robot Paper Man deve aparecer no topo");

        List<Integer> clocks = page.getClockSeconds();
        assertTrue(clocks.size() >= 2, "Devem existir dois relógios (um por jogador), mas há " + clocks.size());
        for (int seconds : clocks) {
            assertTrue(seconds >= 120 && seconds <= 180,
                    "O relógio deve estar perto de 03:00, mas está a " + seconds + " segundos");
        }
    }

    /**
     * CA3.4 – Dado que a partida começou e é a minha vez, então a grelha do adversário
     * mostra a indicação Attack your opponent!.
     */
    @Test
    @DisplayName("CA3.4 – A grelha do adversário mostra Attack your opponent!")
    void ca3_4_indicacaoDeAtaque() {
        page.startRobotGameAsGuest(NICKNAME);
        assertTrue(page.waitForGameScreen(Duration.ofSeconds(10)), "O ecrã de jogo deve abrir");

        assertTrue(page.isAttackYourOpponentDisplayed(),
                "Deve aparecer a indicação Attack your opponent!");
    }

    /**
     * CA3.5 – Dado que ainda não disparei, então existe o botão Abort game; quando já
     * disparei, então passa a existir o botão Resign.
     */
    @Test
    @DisplayName("CA3.5 – Abort game antes de disparar e Resign depois do primeiro disparo")
    void ca3_5_abortAntesResignDepois() {
        page.startRobotGameAsGuest(NICKNAME);
        assertTrue(page.waitForGameScreen(Duration.ofSeconds(10)), "O ecrã de jogo deve abrir");

        assertTrue(page.isAbortGameButtonDisplayed(), "Antes de disparar deve existir Abort game");
        assertFalse(page.isResignButtonDisplayedNow(), "Antes de disparar não deve existir Resign");
        assertTrue(page.isAttackYourOpponentDisplayed(), "Deve ser a minha vez de disparar");

        page.fireAtCell(4, 4);

        assertTrue(page.isResignButtonDisplayed(), "Depois de disparar deve existir Resign");
    }
}