-- Mejeriprodukter
INSERT INTO FoodGroups (name, langual_code) VALUES ('Ost', 'A0784');
UPDATE FoodGroups SET parent_id = 84 WHERE langual_code = 'A0310';
UPDATE FoodGroups SET parent_id = 84 WHERE langual_code = 'A0311';
UPDATE FoodGroups SET parent_id = 84 WHERE langual_code = 'A0312';
UPDATE FoodGroups SET parent_id = 84 WHERE langual_code = 'A0314';
UPDATE FoodGroups SET parent_id = 6 WHERE langual_code = 'A0780';
UPDATE FoodGroups SET parent_id = 7 WHERE langual_code = 'A0781';
UPDATE FoodGroups SET parent_id = 7 WHERE langual_code = 'A0782';
UPDATE FoodGroups SET parent_id = 6 WHERE langual_code = 'A0783';
UPDATE FoodGroups SET parent_id = 84 WHERE langual_code = 'A0786';
UPDATE FoodGroups SET parent_id = 84 WHERE langual_code = 'A0787';
UPDATE FoodGroups SET parent_id = 84 WHERE langual_code = 'A0788';
UPDATE FoodGroups SET parent_id = 6 WHERE langual_code = 'A0789';
-- Äggprodukter
INSERT INTO FoodGroups (name, langual_code) VALUES ('Ägg eller äggprodukt', 'A0790');
UPDATE FoodGroups SET parent_id = 85 WHERE langual_code = 'A0791';
UPDATE FoodGroups SET parent_id = 85 WHERE langual_code = 'A0792';
-- Kött
UPDATE FoodGroups SET parent_id = 16 WHERE langual_code = 'A0794';
UPDATE FoodGroups SET parent_id = 16 WHERE langual_code = 'A0795';
UPDATE FoodGroups SET parent_id = 16 WHERE langual_code = 'A0796';
UPDATE FoodGroups SET parent_id = 16 WHERE langual_code = 'A0797';
UPDATE FoodGroups SET parent_id = 16 WHERE langual_code = 'A0798';
UPDATE FoodGroups SET parent_id = 16 WHERE langual_code = 'A0799';
UPDATE FoodGroups SET parent_id = 16 WHERE langual_code = 'A0800';
-- Fisk och skaldjur
UPDATE FoodGroups SET parent_id = 25 WHERE langual_code = 'A0802';
UPDATE FoodGroups SET parent_id = 25 WHERE langual_code = 'A0803';
UPDATE FoodGroups SET parent_id = 25 WHERE langual_code = 'A0804';
-- Fett eller olja
INSERT INTO FoodGroups (name, langual_code) VALUES ('Fett eller olja', 'A0805');
UPDATE FoodGroups SET parent_id = 86 WHERE langual_code = 'A0806';
UPDATE FoodGroups SET parent_id = 86 WHERE langual_code = 'A0807';
INSERT INTO FoodGroups (name, langual_code) VALUES ('Animaliska fetter', 'A0808');
UPDATE FoodGroups SET parent_id = 87 WHERE langual_code = 'A0809';
UPDATE FoodGroups SET parent_id = 87 WHERE langual_code = 'A0810';
-- Spannmål och spannmålsprodukter
UPDATE FoodGroups SET parent_id = 33 WHERE langual_code = 'A0813';
UPDATE FoodGroups SET parent_id = 33 WHERE langual_code = 'A0814';
UPDATE FoodGroups SET parent_id = 33 WHERE langual_code = 'A0815';
UPDATE FoodGroups SET parent_id = 33 WHERE langual_code = 'A0816';
INSERT INTO FoodGroups (name, langual_code) VALUES ('Bröd och liknande produkter', 'A0817');
UPDATE FoodGroups SET parent_id = 88 WHERE langual_code = 'A0818';
UPDATE FoodGroups SET parent_id = 88 WHERE langual_code = 'A0819';
UPDATE FoodGroups SET parent_id = 88 WHERE langual_code = 'A0820';
UPDATE FoodGroups SET parent_id = 33 WHERE langual_code = 'A0821';
UPDATE FoodGroups SET parent_id = 33, name = 'Cerealierätter t.ex. klimp, risotto, pizza' WHERE langual_code = 'A0822';
-- Nöt, frö eller kärna
UPDATE FoodGroups SET parent_id = 43 WHERE langual_code = 'A0824';
-- Grönsaker, rotfrukter och svamp
UPDATE FoodGroups SET parent_id = 45 WHERE langual_code = 'A0827';
UPDATE FoodGroups SET parent_id = 45 WHERE langual_code = 'A0828';
UPDATE FoodGroups SET parent_id = 48 WHERE langual_code = 'A0830';
UPDATE FoodGroups SET parent_id = 50 WHERE langual_code = 'A0832';
-- Frukt och bär
UPDATE FoodGroups SET parent_id = 52 WHERE langual_code = 'A0834';
-- Socker och söta livsmedel
UPDATE FoodGroups SET parent_id = 54 WHERE langual_code = 'A0836';
UPDATE FoodGroups SET parent_id = 54 WHERE langual_code = 'A0837';
UPDATE FoodGroups SET parent_id = 54, name = 'Konfekt och annan sockerprodukt dvs ej choklad' WHERE langual_code = 'A0838';
UPDATE FoodGroups SET parent_id = 54 WHERE langual_code = 'A0839';
-- Dryck (ej mjölk)
INSERT INTO FoodGroups (name, langual_code) VALUES ('Dryck (ej mjölk)', 'A0840');
UPDATE FoodGroups SET parent_id = 89 WHERE langual_code = 'A0841';
UPDATE FoodGroups SET parent_id = 89 WHERE langual_code = 'A0842';
UPDATE FoodGroups SET parent_id = 60 WHERE langual_code = 'A0843';
UPDATE FoodGroups SET parent_id = 60 WHERE langual_code = 'A0844';
UPDATE FoodGroups SET parent_id = 60 WHERE langual_code = 'A0845';
INSERT INTO FoodGroups (name, langual_code) VALUES ('Dryck med alkohol', 'A0846');
UPDATE FoodGroups SET parent_id = 89 WHERE langual_code = 'A0846';
UPDATE FoodGroups SET parent_id = 90 WHERE langual_code = 'A0847';
UPDATE FoodGroups SET parent_id = 90 WHERE langual_code = 'A0848';
UPDATE FoodGroups SET parent_id = 90 WHERE langual_code = 'A0849';
UPDATE FoodGroups SET parent_id = 90 WHERE langual_code = 'A0850';
-- Övrig matprodukt
UPDATE FoodGroups SET parent_id = 68 WHERE langual_code = 'A0854';
UPDATE FoodGroups SET parent_id = 68 WHERE langual_code = 'A0856';
UPDATE FoodGroups SET parent_id = 68 WHERE langual_code = 'A0857';
UPDATE FoodGroups SET parent_id = 68 WHERE langual_code = 'A0858';
UPDATE FoodGroups SET parent_id = 68 WHERE langual_code = 'A0859';
UPDATE FoodGroups SET parent_id = 72 WHERE langual_code = 'A0860';
UPDATE FoodGroups SET parent_id = 75 WHERE langual_code = 'A0862';
UPDATE FoodGroups SET parent_id = 75 WHERE langual_code = 'A0863';
UPDATE FoodGroups SET parent_id = 75 WHERE langual_code = 'A0864';
UPDATE FoodGroups SET parent_id = 75 WHERE langual_code = 'A0865';
UPDATE FoodGroups SET parent_id = 75 WHERE langual_code = 'A0866';
UPDATE FoodGroups SET parent_id = 75 WHERE langual_code = 'A0868';
-- Övriga livsmedel
UPDATE FoodGroups SET parent_id = 82 WHERE langual_code = 'A0870';
