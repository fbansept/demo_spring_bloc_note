INSERT INTO `utilisateur` (`id`, `mot_de_passe`, `pseudo`) VALUES
(1, '$2a$10$uz5dB8kKtjb37GwBLXUtEeALDOq4Ge/DHx2CmXWOf.hd1ave7Al0i', 'franck'),
(2, '$2a$10$uz5dB8kKtjb37GwBLXUtEeALDOq4Ge/DHx2CmXWOf.hd1ave7Al0i', 'john'),
(3, '$2a$10$uz5dB8kKtjb37GwBLXUtEeALDOq4Ge/DHx2CmXWOf.hd1ave7Al0i', 'toto');


INSERT INTO `note` (`id`, `titre`, `editeur_id`) VALUES
(1, 'Pulp fiction : Jules Winnfield', 1),
(2, 'Kaamelot : Cesar', 1),
(3, 'Koro-sensei', 1),
(4, 'bucky', 1),
(5, 'Citations en vrac', 1),
(6, 'Powerpuff girls', 1);

INSERT INTO `note_liste` (`trier_par_etat`, `id`) VALUES
(b'1', 4),
(b'0', 5),
(b'1', 6);

INSERT INTO `note_texte` (`texte`, `url`, `id`) VALUES
('J’abattrai alors le bras d’une terrible colère, d’une vengeance furieuse et effrayante sur les hordes impies qui pourchassent et réduisent à néant les brebis de Dieu. Et tu connaîtras pourquoi mon nom est l’éternel quand sur toi s’abattra la vengeance du Tout-Puissant', NULL, 1),
('Des chefs de guerre, il y en a de toutes sortes. Des bons, des mauvais, des pleines cagettes, il y en a. Mais une fois de temps en temps, il en sort un. Exceptionnel. Un héros. Une légende. Des chefs comme ça, il n\'y en a presque jamais. Mais tu sais ce qu\'ils ont tous en commun ? Tu sais ce que c\'est, leur pouvoir secret ? Ils ne se battent que pour la dignité des faibles.', NULL, 2),
('Il est dangereux pour eux de grandir sans expérimenter une véritable concurrence. Parce qu’ils vont continuer à pleurnicher, même sans prendre la question au sérieux. Plus tôt ils connaissent la frustration de la défaite, mieux sera leur croissance.', NULL, 3);


INSERT INTO `role` (`id`, `denomination`) VALUES
(1, 'ROLE_UTILISATEUR'),
(2, 'ROLE_ADMINISTRATEUR');


INSERT INTO `tache` (`id`, `termine`, `texte`, `note_id`) VALUES
(1, b'1', 'rusted', 4),
(2, b'0', 'furnace', 4),
(3, b'1', 'daybreak', 4),
(4, b'0', 'seventeen', 4),
(5, b'1', 'benign', 4),
(6, b'0', 'nine', 4),
(7, b'1', 'homecoming', 4),
(8, b'1', 'one', 4),
(9, b'1', 'freight car', 4),

(10, b'0', 'La vie c’est comme une boîte de chocolats, on ne sait jamais sur quoi on va tomber.', 5),
(11, b'1', 'C’est à une demi-heure d’ici. J’y suis dans dix minutes', 5),
(12, b'1', 'Tu vois, le monde se divise en deux catégories: ceux qui ont un pistolet chargé et ceux qui creusent. Toi tu creuses.', 5),
(13, b'0', 'Tout ce que voulez, mais de pas le costume vert', 5),
(14, b'1', 'J’adore l’odeur du napalm au petit matin. ', 5),
(15, b'0', 'Allo McFly, y’a personne au bout du fil ! Faut réfléchir McFLy ! ', 5),
(16, b'1', 'Leilou Dallas, multipass. Muultiipaass', 5),

(17, b'1', 'Du sucre', 6),
(18, b'0', 'Des epices', 6),
(19, b'1', 'Et des tas de bonnes chose', 6);

INSERT INTO `utilisateur_role` (`utilisateur_id`, `role_id`) VALUES
(1, 1),
(2, 2);

INSERT INTO `historique` (`date`, `note_id`, `utilisateur_id`, `action`) VALUES
('2021-05-04 00:00:00', '1', '1', 'Ajout de la note');