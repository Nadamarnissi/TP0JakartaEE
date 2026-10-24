package ma.emsi.marnissi.service;

import jakarta.enterprise.context.Dependent;

import java.io.Serializable;
import java.util.Locale;

/**
 * Classe de service pour les modificateurs de question.
 * Un modificateur de question prend en entrée une question et retourne
 * la question modifiée.
 */
@Dependent
public class GenerateurReponse implements Serializable {

    private boolean roleSystemeAffiche = false;

    /**
     * Génère une réponse à une question.
     *
     * @param question La question à modifier.
     * @param roleSysteme Le rôle système à utiliser.
     * @return La question modifiée, entourée de "||".
     */
    public String genererReponse(String question, String roleSysteme) {

        String resultat = "||";

        if (!this.roleSystemeAffiche && roleSysteme != null) {

            // Afficher le rôle système uniquement à la première réponse.
            resultat += roleSysteme.toUpperCase(Locale.FRENCH) + "\n";

            this.roleSystemeAffiche = true;
        }

        resultat += question.toLowerCase(Locale.FRENCH) + "||";

        return resultat;
    }
}
