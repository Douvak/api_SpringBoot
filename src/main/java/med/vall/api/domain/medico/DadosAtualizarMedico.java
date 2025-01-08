package med.vall.api.domain.medico;

import jakarta.validation.constraints.NotNull;
import med.vall.api.domain.endereco.DadosEndereco;

public record DadosAtualizarMedico(
        @NotNull
        Long id,

        String nome,
        DadosEndereco endereco) {
}
