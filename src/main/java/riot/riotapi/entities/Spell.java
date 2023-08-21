package riot.riotapi.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "spell")
public class Spell {
    @Id
    @Column(name = "spell_id")
    private Integer spellId;
    @Column(name = "spell")
    private String spell;
    @Column(name = "emoji")
    private String emoji;

    public Spell(String spell) {
        this.spell = spell;
    }
}
