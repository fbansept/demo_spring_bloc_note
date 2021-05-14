SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM utilisateur_role WHERE 1 = 1;
DELETE FROM utilisateur WHERE 1 = 1;
DELETE FROM role WHERE 1 = 1;

SET FOREIGN_KEY_CHECKS = 1;

ALTER TABLE utilisateur AUTO_INCREMENT = 1;
ALTER TABLE role AUTO_INCREMENT = 1;

INSERT INTO `utilisateur` (`id`, `pseudo`, `mot_de_passe`)
VALUES  (1, 'franck', '$2a$10$uz5dB8kKtjb37GwBLXUtEeALDOq4Ge/DHx2CmXWOf.hd1ave7Al0i'),
        (2, 'john', '$2a$10$uz5dB8kKtjb37GwBLXUtEeALDOq4Ge/DHx2CmXWOf.hd1ave7Al0i'),
        (3, 'toto', '$2a$10$uz5dB8kKtjb37GwBLXUtEeALDOq4Ge/DHx2CmXWOf.hd1ave7Al0i');

INSERT INTO `role` (`id`, `denomination`) VALUES
(1, 'ROLE_UTILISATEUR'),
(2, 'ROLE_ADMINISTRATEUR');

INSERT INTO `note` (`id`, `titre`, `editeur_id`) VALUES
(1, 'note 1', 1),
(2, 'note 2', 1),
(3, 'note 3', 1),
(4, 'bucky 1', 1),
(5, 'bucky 2', 1),
(6, 'bucky 3', 1);

INSERT INTO `note_texte` (`id`, `texte`, `url`) VALUES
(1, 'contenu note 1', null),
(2, 'contenu note 2', null),
(3, 'contenu note 3', null);

INSERT INTO `note_liste` (`id`, `trier_par_etat`) VALUES
(4, true),
(5, false),
(6, true);

INSERT INTO `tache` (`id`, `texte`, `note_id`, `termine`) VALUES
(1, "rusted", 4, true),
(2, "furnace", 4, false),
(3, "daybreak", 4, true),
(4, "seventeen", 5, false),
(5, "benign", 5, true),
(6, "nine", 5, false),
(7, "homecoming", 6, true),
(8, "one", 6, true),
(9, "freight car", 6, true);


INSERT INTO `utilisateur_role` (`utilisateur_id`, `role_id`)
VALUES ('1', '1'), ('2', '2');