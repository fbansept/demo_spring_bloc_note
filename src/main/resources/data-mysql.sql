-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3307
-- Généré le :  Dim 23 mai 2021 à 20:56
-- Version du serveur :  10.1.40-MariaDB
-- Version de PHP :  7.3.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `demo_spring_bloc_note`
--

-- --------------------------------------------------------

--
-- Structure de la table `note`
--

DROP TABLE IF EXISTS `note`;
CREATE TABLE IF NOT EXISTS `note` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `titre` varchar(255) DEFAULT NULL,
  `editeur_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKn3twoxayclbf61mlve4ilh8xf` (`editeur_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `note`
--

INSERT INTO `note` (`id`, `titre`, `editeur_id`) VALUES
(1, 'note 1', 1),
(2, 'note 2', 1),
(3, 'note 3', 1),
(4, 'bucky 1', 1),
(5, 'bucky 2', 1),
(6, 'bucky 3', 1);

-- --------------------------------------------------------

--
-- Structure de la table `note_liste`
--

DROP TABLE IF EXISTS `note_liste`;
CREATE TABLE IF NOT EXISTS `note_liste` (
  `trier_par_etat` bit(1) NOT NULL,
  `id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `note_liste`
--

INSERT INTO `note_liste` (`trier_par_etat`, `id`) VALUES
(b'1', 4),
(b'0', 5),
(b'1', 6);

-- --------------------------------------------------------

--
-- Structure de la table `note_texte`
--

DROP TABLE IF EXISTS `note_texte`;
CREATE TABLE IF NOT EXISTS `note_texte` (
  `texte` text,
  `url` varchar(255) DEFAULT NULL,
  `id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `note_texte`
--

INSERT INTO `note_texte` (`texte`, `url`, `id`) VALUES
('contenu note 1', NULL, 1),
('contenu note 2', NULL, 2),
('contenu note 3', NULL, 3);

-- --------------------------------------------------------

--
-- Structure de la table `role`
--

DROP TABLE IF EXISTS `role`;
CREATE TABLE IF NOT EXISTS `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `denomination` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `role`
--

INSERT INTO `role` (`id`, `denomination`) VALUES
(1, 'ROLE_UTILISATEUR'),
(2, 'ROLE_ADMINISTRATEUR');

-- --------------------------------------------------------

--
-- Structure de la table `tache`
--

DROP TABLE IF EXISTS `tache`;
CREATE TABLE IF NOT EXISTS `tache` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `termine` bit(1) NOT NULL,
  `texte` varchar(255) DEFAULT NULL,
  `note_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKs300iv8ldcw9yqg43ke4te0wo` (`note_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `tache`
--

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

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `age` int(11) DEFAULT NULL,
  `mot_de_passe` varchar(255) DEFAULT NULL,
  `pseudo` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

DROP TABLE IF EXISTS `utilisateur`;
CREATE TABLE IF NOT EXISTS `utilisateur` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mot_de_passe` varchar(255) DEFAULT NULL,
  `pseudo` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `utilisateur`
--

INSERT INTO `utilisateur` (`id`, `mot_de_passe`, `pseudo`) VALUES
(1, '$2a$10$uz5dB8kKtjb37GwBLXUtEeALDOq4Ge/DHx2CmXWOf.hd1ave7Al0i', 'franck'),
(2, '$2a$10$uz5dB8kKtjb37GwBLXUtEeALDOq4Ge/DHx2CmXWOf.hd1ave7Al0i', 'john'),
(3, '$2a$10$uz5dB8kKtjb37GwBLXUtEeALDOq4Ge/DHx2CmXWOf.hd1ave7Al0i', 'toto');

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur_role`
--

DROP TABLE IF EXISTS `utilisateur_role`;
CREATE TABLE IF NOT EXISTS `utilisateur_role` (
  `utilisateur_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`utilisateur_id`,`role_id`),
  KEY `FKad9wf1u7gjbx2p2y9hs8ow39x` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `utilisateur_role`
--

INSERT INTO `utilisateur_role` (`utilisateur_id`, `role_id`) VALUES
(1, 1),
(2, 2);

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `note`
--
ALTER TABLE `note`
  ADD CONSTRAINT `FKn3twoxayclbf61mlve4ilh8xf` FOREIGN KEY (`editeur_id`) REFERENCES `utilisateur` (`id`);

--
-- Contraintes pour la table `note_liste`
--
ALTER TABLE `note_liste`
  ADD CONSTRAINT `FK13brthwpyr3r0j8dr0fwq3grv` FOREIGN KEY (`id`) REFERENCES `note` (`id`);

--
-- Contraintes pour la table `note_texte`
--
ALTER TABLE `note_texte`
  ADD CONSTRAINT `FKetydwolc9mek50poq9fm7i06w` FOREIGN KEY (`id`) REFERENCES `note` (`id`);

--
-- Contraintes pour la table `tache`
--
ALTER TABLE `tache`
  ADD CONSTRAINT `FKs300iv8ldcw9yqg43ke4te0wo` FOREIGN KEY (`note_id`) REFERENCES `note` (`id`);

--
-- Contraintes pour la table `utilisateur_role`
--
ALTER TABLE `utilisateur_role`
  ADD CONSTRAINT `FK6kifvrsfkpqn502r5ipjl5pvu` FOREIGN KEY (`utilisateur_id`) REFERENCES `utilisateur` (`id`),
  ADD CONSTRAINT `FKad9wf1u7gjbx2p2y9hs8ow39x` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
