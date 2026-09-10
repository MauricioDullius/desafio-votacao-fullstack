CREATE TABLE vote (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      agenda_id BIGINT NOT NULL,
                      member_id BIGINT NOT NULL,
                      vote INT NOT NULL,

                      CONSTRAINT fk_vote_agenda FOREIGN KEY (agenda_id)
                          REFERENCES agenda(id)
                          ON DELETE CASCADE,

                      CONSTRAINT fk_vote_member FOREIGN KEY (member_id)
                          REFERENCES member(id)
                          ON DELETE CASCADE,

                      CONSTRAINT uc_vote UNIQUE (agenda_id, member_id)
);