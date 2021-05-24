INSERT INTO `utilisateur` (`id`, `mot_de_passe`, `pseudo`) VALUES
(1, '$2a$10$uz5dB8kKtjb37GwBLXUtEeALDOq4Ge/DHx2CmXWOf.hd1ave7Al0i', 'franck'),
(2, '$2a$10$uz5dB8kKtjb37GwBLXUtEeALDOq4Ge/DHx2CmXWOf.hd1ave7Al0i', 'john'),
(3, '$2a$10$uz5dB8kKtjb37GwBLXUtEeALDOq4Ge/DHx2CmXWOf.hd1ave7Al0i', 'toto');


INSERT INTO `note` (`id`, `titre`, `editeur_id`) VALUES
(1, 'note 1', 1),
(2, 'note 2', 1),
(3, 'note 3', 1),
(4, 'bucky 1', 1),
(5, 'bucky 2', 1),
(6, 'bucky 3', 1);

INSERT INTO `note_liste` (`trier_par_etat`, `id`) VALUES
(b'1', 4),
(b'0', 5),
(b'1', 6);

INSERT INTO `note_texte` (`texte`, `url`, `id`) VALUES
('contenu note 1', NULL, 1),
('contenu note 2', NULL, 2),
('contenu note 3', NULL, 3);


INSERT INTO `role` (`id`, `denomination`) VALUES
(1, 'ROLE_UTILISATEUR'),
(2, 'ROLE_ADMINISTRATEUR');


INSERT INTO `tache` (`id`, `termine`, `texte`, `note_id`) VALUES
(1, b'1', 'rusted', 4),
(2, b'0', 'furnace', 4),
(3, b'1', 'daybreak', 4),
(4, b'0', 'seventeen', 5),
(5, b'1', 'benign', 5),
(6, b'0', 'nine', 5),
(7, b'1', 'homecoming', 6),
(8, b'1', 'one', 6),
(9, b'1', 'freight car', 6);

INSERT INTO `utilisateur_role` (`utilisateur_id`, `role_id`) VALUES
(1, 1),
(2, 2);