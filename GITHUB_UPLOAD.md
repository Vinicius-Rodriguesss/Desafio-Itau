# 🚀 Guia para Subir no GitHub

## Pré-requisitos

1. **Git instalado** - Baixe em https://git-scm.com/
2. **Conta GitHub** - Crie em https://github.com/signup
3. **Estar na pasta do projeto** - `C:\Users\Vinicius Rodrigues\Downloads\desafio-itau\desafio-itau`

---

## Passo 1: Configurar Git (primeira vez)

Abra **Windows cmd** ou **PowerShell** e configure seu Git:

```cmd
git config --global user.name "Seu Nome"
git config --global user.email "seu.email@exemplo.com"
```

Substitua com seus dados reais.

---

## Passo 2: Inicializar repositório local

Na pasta do projeto, execute:

```cmd
git init
```

Isso cria um repositório Git local (pasta `.git` oculta).

---

## Passo 3: Adicionar todos os arquivos

```cmd
git add .
```

Isso prepara todos os arquivos para commit (incluindo `.gitignore` que ignora /target e outros).

---

## Passo 4: Fazer primeiro commit

```cmd
git commit -m "Initial commit - Desafio Itau API de Transações e Estatísticas"
```

---

## Passo 5: Criar repositório no GitHub

1. Acesse https://github.com/new
2. Preencha:
   - **Repository name**: `desafio-itau`
   - **Description**: `API REST de transações bancárias em tempo real com estatísticas`
   - **Visibility**: Choose **Public** (ou Private se preferir)
   - **Initialize**: Deixe sem marcar (você já iniciou localmente)
3. Clique em **Create repository**

Copie a URL que aparecer (parecida com: `https://github.com/SEU_USUARIO/desafio-itau.git`)

---

## Passo 6: Conectar repositório remoto

Execute no cmd (substitua `SEU_USUARIO` por seu username do GitHub):

```cmd
git remote add origin https://github.com/SEU_USUARIO/desafio-itau.git
```

Verifique se funcionou:

```cmd
git remote -v
```

Deve mostrar:
```
origin  https://github.com/SEU_USUARIO/desafio-itau.git (fetch)
origin  https://github.com/SEU_USUARIO/desafio-itau.git (push)
```

---

## Passo 7: Renomear branch (se necessário)

Se estiver em `master`, renomeie para `main` (padrão moderno):

```cmd
git branch -M main
```

---

## Passo 8: Fazer primeiro push

Envie o código para GitHub:

```cmd
git push -u origin main
```

Na primeira vez, o Git pedirá autenticação:
- **Usuario**: Seu username do GitHub
- **Senha**: Use um **Personal Access Token** (não sua senha real)

### Gerar Personal Access Token (PAT)

Se o Git pedir token:

1. Acesse https://github.com/settings/tokens/new
2. Preencha:
   - **Token name**: `git-cli`
   - **Expiration**: 30 days (ou conforme preferir)
   - **Scopes**: Marque `repo` e `workflow`
3. Clique em **Generate token** e **copie o token**
4. Cole no prompt do cmd quando pedir

---

## ✅ Pronto!

Seu repositório está no GitHub. Acesse:

```
https://github.com/SEU_USUARIO/desafio-itau
```

---

## 🔄 Próximos Commits (atualizações)

Sempre que fizer mudança:

```cmd
git add .
git commit -m "Descrição da mudança"
git push origin main
```

---

## 📌 Comandos Úteis

**Ver status** (quais arquivos mudaram):
```cmd
git status
```

**Ver histórico de commits**:
```cmd
git log --oneline
```

**Desfazer mudança local** (antes de commit):
```cmd
git checkout -- arquivo.java
```

**Desfazer último commit** (ainda não pushou):
```cmd
git reset --soft HEAD~1
```

---

## ❓ Troubleshooting

### "fatal: could not read Username"
- Use **Personal Access Token**, não senha do GitHub
- Gere um em https://github.com/settings/tokens

### "Permission denied (publickey)"
- Você usa SSH em vez de HTTPS
- Gere chave SSH em: https://github.com/settings/keys

### "Updates were rejected"
- Alguém fez push antes de você
- Execute: `git pull origin main` depois `git push origin main`

---

## 🎉 Pronto para Entregar!

Seu código está versionado e publicado. Compartilhe o link do repositório!

