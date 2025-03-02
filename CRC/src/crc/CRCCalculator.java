package crc;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CRCCalculator extends JFrame {

    private static final long serialVersionUID = 1L;

    /**
     * Cette fonction permet de faire l'addition binaire (XOR) entre deux nombres.
     * @param bit1 Premier bit
     * @param bit2 Deuxième bit
     * @return Résultat XOR entre bit1 et bit2
     */
    private static int xorBinaire(int bit1, int bit2) {
        return (bit1 == bit2) ? 0 : 1;
    }

    /**
     * Cette fonction retourne l'indice du premier '1' dans la liste donnée.
     * @param listeBits Liste binaire
     * @return Indice du premier bit à 1
     */
    private static int trouverPremierUn(List<Integer> listeBits) {
        int index = 0;
        while (index < listeBits.size() && listeBits.get(index) != 1) {
            index++;
        }
        return index;
    }

    /**
     * Effectue le calcul du code CRC en utilisant la division binaire.
     * @param trame La trame binaire sous forme de chaîne de caractères
     * @param polynomeGenerateur Le polynôme générateur sous forme de liste d'entiers binaires
     * @return Liste contenant toutes les étapes de la division CRC
     */
    public static List<List<Integer>> calculerCRC(String trame, List<Integer> polynomeGenerateur) {
        // Convertir la trame en liste d'entiers
        List<Integer> bitsTrame = new ArrayList<>(
                Arrays.stream(trame.split(""))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList())
        );

        int degrePolynome = polynomeGenerateur.size() - 1;

        // Ajouter des zéros à la fin de la trame (selon le degré du polynôme générateur)
        for (int i = 0; i < degrePolynome; i++) {
            bitsTrame.add(0);
        }

        // Stocker les étapes de la division
        List<List<Integer>> etapesDivision = new ArrayList<>();
        etapesDivision.add(new ArrayList<>(bitsTrame)); // Ajouter l'état initial

        // Opération de division binaire
        List<Integer> reste = new ArrayList<>(bitsTrame); // Copie pour manipuler le reste
        int tailleReste = bitsTrame.size();

        while (tailleReste >= polynomeGenerateur.size()) {
            int premierUn = trouverPremierUn(reste);

            // Vérifier si on peut encore soustraire le polynôme générateur
            if (premierUn + polynomeGenerateur.size() > reste.size()) {
                break;
            }

            // Appliquer l'opération XOR
            for (int i = 0; i < polynomeGenerateur.size(); i++) {
                reste.set(premierUn + i, xorBinaire(reste.get(premierUn + i), polynomeGenerateur.get(i)));
            }

            // Ajouter l'étape actuelle à la liste
            etapesDivision.add(new ArrayList<>(reste));

            // Mettre à jour la taille du reste
            tailleReste = reste.size() - trouverPremierUn(reste);
        }

        return etapesDivision;
    }
    

    /**
     * Vérifie si le message reçu contient une erreur en recalculant le CRC.
     * @param messageRecu Le message reçu (trame + CRC)
     * @param polynomeGenerateur Le polynôme générateur
     * @return Liste contenant toutes les étapes de la vérification CRC
     */
    public static List<List<Integer>> verifierCRC(String messageRecu, List<Integer> polynomeGenerateur) {
        // Convertir la trame en liste d'entiers
        List<Integer> bitsMessageRecu = new ArrayList<>(
                Arrays.stream(messageRecu.split(""))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList())
        );

        // Stocker les étapes de la division
        List<List<Integer>> etapesVerification = new ArrayList<>();
        etapesVerification.add(new ArrayList<>(bitsMessageRecu)); // Ajouter l'état initial

        // Opération de division binaire pour vérifier le CRC
        List<Integer> reste = new ArrayList<>(bitsMessageRecu);
        int tailleReste = bitsMessageRecu.size();

        while (tailleReste >= polynomeGenerateur.size()) {
            int premierUn = trouverPremierUn(reste);

            // Vérifier si on peut encore soustraire le polynôme générateur
            if (premierUn + polynomeGenerateur.size() > reste.size()) {
                break;
            }

            // Appliquer l'opération XOR
            for (int i = 0; i < polynomeGenerateur.size(); i++) {
                reste.set(premierUn + i, xorBinaire(reste.get(premierUn + i), polynomeGenerateur.get(i)));
            }

            // Ajouter l'étape actuelle à la liste
            etapesVerification.add(new ArrayList<>(reste));

            // Mettre à jour la taille du reste
            tailleReste = reste.size() - trouverPremierUn(reste);
        }

        return etapesVerification;
    }
}