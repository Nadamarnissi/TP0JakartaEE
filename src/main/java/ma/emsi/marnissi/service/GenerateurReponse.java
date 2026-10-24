
package ma.emsi.marnissi.service;

import jakarta.enterprise.context.Dependent;

import java.io.Serializable;
import java.text.BreakIterator;
import java.text.Normalizer;
import java.util.Locale;

/**
 * Service CDI de génération des réponses.
 *
 * Bonus TP0 : analyse linguistique et statistique
 * de la question saisie par l'utilisateur.
 */
@Dependent
public class GenerateurReponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean roleSystemeAffiche = false;

    /**
     * Analyse la question et produit une réponse personnalisée.
     *
     * Le rôle système est conservé dans la première réponse,
     * conformément au comportement du code officiel.
     */
    public String genererReponse(String question, String roleSysteme) {
        String texte = question == null ? "" : question.strip();

        if (texte.isEmpty()) {
            return "Veuillez saisir une question.";
        }

        int nombreMots = 0;
        String motLePlusLong = "";

        // BreakIterator gère mieux les mots Unicode qu'un split sur les espaces.
        BreakIterator iterateur = BreakIterator.getWordInstance(Locale.FRENCH);
        iterateur.setText(texte);

        int debut = iterateur.first();

        for (int fin = iterateur.next();
             fin != BreakIterator.DONE;
             debut = fin, fin = iterateur.next()) {

            String segment = texte.substring(debut, fin);

            if (segment.codePoints().anyMatch(Character::isLetterOrDigit)) {
                nombreMots++;

                if (segment.codePointCount(0, segment.length())
                        > motLePlusLong.codePointCount(0, motLePlusLong.length())) {
                    motLePlusLong = segment;
                }
            }
        }

        // Normalisation pour compter les voyelles accentuées.
        String normalise = Normalizer.normalize(
                texte.toLowerCase(Locale.FRENCH),
                Normalizer.Form.NFD
        ).replaceAll("\\p{M}+", "");

        long nombreVoyelles = normalise.codePoints()
                .filter(c -> c == 'a' || c == 'e' || c == 'i'
                        || c == 'o' || c == 'u' || c == 'y')
                .count();

        int nombreCaracteres = texte.codePointCount(0, texte.length());

        // Inversion par points de code pour éviter de couper les paires UTF-16.
        int[] caracteres = texte.codePoints().toArray();
        StringBuilder texteInverse = new StringBuilder();

        for (int i = caracteres.length - 1; i >= 0; i--) {
            texteInverse.appendCodePoint(caracteres[i]);
        }

        StringBuilder resultat = new StringBuilder();

        if (!roleSystemeAffiche && roleSysteme != null) {
            resultat.append("RÔLE SYSTÈME : ")
                    .append(roleSysteme.toUpperCase(Locale.FRENCH))
                    .append("\n\n");

            roleSystemeAffiche = true;
        }

        resultat.append("=== ANALYSE DE VOTRE TEXTE ===\n\n")
                .append("Texte : ").append(texte).append("\n")
                .append("Nombre de mots : ").append(nombreMots).append("\n")
                .append("Nombre de caractères : ").append(nombreCaracteres).append("\n")
                .append("Nombre de voyelles : ").append(nombreVoyelles).append("\n")
                .append("Mot le plus long : ").append(motLePlusLong).append("\n")
                .append("Texte inversé : ").append(texteInverse).append("\n");

        return resultat.toString();
    }
}
