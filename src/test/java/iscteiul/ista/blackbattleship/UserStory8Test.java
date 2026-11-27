package iscteiul.ista.blackbattleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Page Test Class da user story US8 – Disparar sobre o adversário.
 * <p>
 * Testes de aceitação (caixa-preta) derivados do cenário {@code UserStoryTest8}
 * do ficheiro {@code TestSuite_129873.side}. Todos os testes começam com uma
 * partida contra o robot já iniciada e com as duas grelhas desenhadas.
 * </p>
 */
class UserStory8Test {

    /** Nickname usado nos testes. */
    private static final String NICKNAME = "TesteMartim";

    /** Casa usada nos disparos (linha 4, coluna 5, a mesma do cenário gravado). */
    private static final int ROW = 4;
    private static final int COLUMN = 5;

    /** Tempo a esperar para o site reagir a um clique que não deve ter efeito. */
    private static final long NO_EFFECT_WAIT_MS = 2500;

    private WebDriver driver;
    private UserStory8 page;

    /** Abre um browser novo, inicia uma partida contra o robot e espera pelas grelhas. */
    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().setSize(new Dimension(1400, 900));
        page = new UserStory8(driver);
        page.open();
        page.acceptCookiesIfPresent();
        page.startRobotGameAsGuest(NICKNAME);

        assertTrue(page.waitForGameScreen(Duration.ofSeconds(10)), "O ecrã de jogo deve abrir");
        assertTrue(page.waitForBoards(), "As duas grelhas devem ser desenhadas");
    }

    /** Fecha o browser no fim de cada teste. */
    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * CA8.1 – Dado que é a minha vez, quando clico numa casa da grelha do adversário,
     * então o disparo é registado e a casa fica marcada como acerto ou falhanço.
     */
    @Test
    @DisplayName("CA8.1 – Disparar numa casa do adversário marca a casa")
    void ca8_1_disparoMarcaACasa() {
        int before = page.countShotMarkers(UserStory8.OPPONENT_BOARD);

        page.fireAtOpponentCell(ROW, COLUMN);

        assertTrue(page.waitForShotMarkers(UserStory8.OPPONENT_BOARD, before + 1, Duration.ofSeconds(8)),
                "O disparo deve ser registado na grelha do adversário. HTML da casa: "
                        + page.getCellHtml(UserStory8.OPPONENT_BOARD, ROW, COLUMN));
        assertTrue(page.isCellMarked(UserStory8.OPPONENT_BOARD, ROW, COLUMN),
                "A casa atacada deve ficar marcada como acerto ou falhanço. HTML da casa: "
                        + page.getCellHtml(UserStory8.OPPONENT_BOARD, ROW, COLUMN));
    }

    /**
     * CA8.2 – Dado que já disparei numa casa, quando volto a clicar na mesma casa,
     * então não é registado um novo disparo.
     */
    @Test
    @DisplayName("CA8.2 – Voltar a clicar na mesma casa não dispara outra vez")
    void ca8_2_mesmaCasaNaoDisparaDeNovo() {
        page.fireAtOpponentCell(ROW, COLUMN);
        assertTrue(page.waitForShotMarkers(UserStory8.OPPONENT_BOARD, 1, Duration.ofSeconds(8)),
                "O primeiro disparo deve ser registado");
        int markersAfterFirstShot = page.countShotMarkers(UserStory8.OPPONENT_BOARD);

        page.fireAtOpponentCellQuickly(ROW, COLUMN);
        page.waitMillis(NO_EFFECT_WAIT_MS);

        assertEquals(markersAfterFirstShot, page.countShotMarkers(UserStory8.OPPONENT_BOARD),
                "Clicar outra vez na mesma casa não deve registar outro disparo");
    }

    /**
     * CA8.3 – Dado que não é a minha vez (o robot está a jogar), quando clico numa casa
     * da grelha do adversário, então o disparo não é registado.
     * <p>
     * Depende da rapidez do robot: o teste dispara até obter um instante em que a
     * indicação "Attack your opponent!" desaparece e só então clica. Se isso não
     * acontecer em 6 tentativas, o teste é ignorado em vez de falhar.
     * </p>
     */
    @Test
    @DisplayName("CA8.3 – Fora da minha vez, clicar numa casa não dispara")
    void ca8_3_foraDaMinhaVezNaoDispara() {
        int[][] shots = {{1, 1}, {3, 3}, {5, 5}, {7, 7}, {9, 1}, {1, 9}};
        int[] target = {8, 2};
        boolean notMyTurnObserved = false;

        for (int[] shot : shots) {
            page.fireAtOpponentCellQuickly(shot[0], shot[1]);
            if (!page.isMyTurnNow()) {
                notMyTurnObserved = true;
                break;
            }
            page.waitMillis(1500);
        }
        assumeTrue(notMyTurnObserved,
                "Não foi possível apanhar um instante em que não é a minha vez; teste ignorado");

        page.fireAtOpponentCellQuickly(target[0], target[1]);
        page.waitMillis(NO_EFFECT_WAIT_MS);

        assertFalse(page.isCellMarked(UserStory8.OPPONENT_BOARD, target[0], target[1]),
                "Fora da minha vez, a casa clicada não deve ser marcada. HTML da casa: "
                        + page.getCellHtml(UserStory8.OPPONENT_BOARD, target[0], target[1]));
    }

    /**
     * CA8.4 – Dado que é a minha vez, quando clico numa casa da minha própria grelha,
     * então não é registado nenhum disparo.
     */
    @Test
    @DisplayName("CA8.4 – Clicar na minha própria grelha não dispara")
    void ca8_4_cliqueNaMinhaGrelhaNaoDispara() {
        int myMarkersBefore = page.countShotMarkers(UserStory8.MY_BOARD);
        int opponentMarkersBefore = page.countShotMarkers(UserStory8.OPPONENT_BOARD);

        page.clickMyCell(ROW, COLUMN);
        page.waitMillis(NO_EFFECT_WAIT_MS);

        assertEquals(myMarkersBefore, page.countShotMarkers(UserStory8.MY_BOARD),
                "Clicar na minha grelha não deve marcar nenhuma casa. HTML da casa: "
                        + page.getCellHtml(UserStory8.MY_BOARD, ROW, COLUMN));
        assertEquals(opponentMarkersBefore, page.countShotMarkers(UserStory8.OPPONENT_BOARD),
                "Clicar na minha grelha não deve registar disparos no adversário");
    }
}