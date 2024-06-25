public class EditarConta extends AppCompatActivity {

    private Button voltar, salvar, deletar;
    private TextView nome, telefone, email, senha;
    private int usuarioId;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_conta);

        voltar = findViewById(R.id.btn_voltarTelaInicial);
        salvar = findViewById(R.id.btn_salvar);
        deletar = findViewById(R.id.btn_deletar); // Referência ao botão de deletar
        nome = findViewById(R.id.txt_editName);
        telefone = findViewById(R.id.txt_editPhone);
        email = findViewById(R.id.txt_editEmail);
        senha = findViewById(R.id.txt_editPassword);

        voltar.setOnClickListener(v -> startActivity(new Intent(EditarConta.this, TelaInicial.class)));
        salvar.setOnClickListener(v -> salvarConta());
        deletar.setOnClickListener(v -> confirmarDelecao()); // Adiciona um ouvinte de cliques ao botão de deletar

        preencherDadosUsuario();
    }

    private void confirmarDelecao() {
        new AlertDialog.Builder(this)
            .setTitle("Confirmar Deleção")
            .setMessage("Você tem certeza que deseja deletar sua conta? Esta ação é irreversível.")
            .setPositiveButton("Deletar", (dialog, which) -> deletarConta())
            .setNegativeButton("Cancelar", null)
            .show();
    }

    private void deletarConta() {
        UsuarioDao usuarioDao = AppDatabase.getDatabase(this).usuarioDao();
        Usuario usuario = usuarioDao.getUser(usuarioId);
        if (usuario != null) {
            usuarioDao.delete(usuario);
            showToast("Conta deletada com sucesso.");

            // Volta para a tela de login
            Intent intent = new Intent(EditarConta.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            showToast("Erro: usuário não encontrado");
        }
    }

    private void salvarConta() {
        String novoNome = nome.getText().toString();
        String novoTelefone = telefone.getText().toString();
        String novoEmail = email.getText().toString();
        String novaSenha = senha.getText().toString();

        // Obtém o usuário do banco de dados pelo ID
        UsuarioDao usuarioDao = AppDatabase.getDatabase(this).usuarioDao();
        Usuario usuario = usuarioDao.getUser(usuarioId);

        // Verifica se o usuário foi encontrado
        if (usuario != null) {
            // Verifica se houve alterações nos dados do usuário
            if (!usuario.getNome().equals(novoNome) ||
                    !usuario.getTelefone().equals(novoTelefone) ||
                    !usuario.getEmail().equals(novoEmail) ||
                    !usuario.getSenha().equals(novaSenha)) {

                // Atualiza os dados do usuário no banco de dados
                usuario.setNome(novoNome);
                usuario.setTelefone(novoTelefone);
                usuario.setEmail(novoEmail);
                usuario.setSenha(novaSenha);
                usuarioDao.update(usuario);
                showToast("Dados atualizados com sucesso");

                // Volta para a tela inicial
                Intent intent = new Intent(EditarConta.this, TelaInicial.class);
                startActivity(intent);
                finish();
            } else {
                showToast("Nenhuma alteração realizada");
            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
