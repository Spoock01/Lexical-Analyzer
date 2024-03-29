package AnalisadorLexico;

import java.util.ArrayList;


public class Syntax {
   private final ArrayList<Table> tokens;
   private Table currentToken;
   private int nextTokenIndex;
   private final boolean showTokens = false;
   private final ArrayList<IdentifierType> symbolTable;
   private final ArrayList<IdentifierType> symbolTableProcedure;
   private final ArrayList<IdentifierType> variableDeclaration; //Para poder atribuir os tipos a ele na hora da declaração
   private final ArrayList<IdentifierType> stackAttribution;
   private ArrayList<IdentifierType> pList;
   private String opArithmetic;
   private final ArrayList<ParameterType> pType;
   private int indexPType = 0;
   
    public Syntax(ArrayList<Table> tokens){
        this.tokens = tokens;
        this.nextTokenIndex = 0;
        symbolTable = new ArrayList<>();
        symbolTableProcedure = new ArrayList<>();
        variableDeclaration = new ArrayList<>();
        stackAttribution = new ArrayList<>();
        this.pType = new ArrayList<>();
        this.pList = new ArrayList<>();
        opArithmetic = "";
    }
    
    void printVariableDeclaration(){
        
        variableDeclaration.forEach((it) -> {
            System.out.println(it.getIdentifier() + it.getType());
       });
        System.out.println("\n\n");
        
        
    }
    
    void setSymbolType_(){
        
        for(int i = 0; i < variableDeclaration.size(); i++)
            for(int j = symbolTable.size() - 1; j >= 0; j--){
                
                if(variableDeclaration.get(i).getIdentifier().equals(symbolTable.get(j).getIdentifier())
                    && symbolTable.get(j).getType().equals("undefined")){
                    variableDeclaration.get(i).setType(currentToken.getToken());
                    symbolTable.get(j).setType(currentToken.getToken());
                    
                    break;
                    
                }
                
            }

    }
    
    private void setAttributionType(){
        for(int i = 0; i < stackAttribution.size(); i++)
            for(int j = symbolTable.size() - 1; j >= 0; j--){
                
                if(stackAttribution.get(i).getIdentifier().equals(symbolTable.get(j).getIdentifier())){
                    
                    stackAttribution.get(i).setType(symbolTable.get(j).getType());
                    break;
                    
                }
                
            }
               
    }
    
    
    private void checkAttribution(){
        int top = stackAttribution.size() - 1;
        int underTop = top - 1;
        
        setAttributionType();
        
        if(stackAttribution.get(underTop).getType().equals("integer") &&
           stackAttribution.get(top).getType().equals("integer")){
            
            stackAttribution.clear();
        }else if(stackAttribution.get(underTop).getType().equals("real") &&
                 (stackAttribution.get(top).getType().equals("integer") || 
                  stackAttribution.get(top).getType().equals("real"))){
            
            stackAttribution.clear();
        }else if(stackAttribution.get(underTop).getType().equals("boolean") &&
                 stackAttribution.get(top).getType().equals("boolean")){
            
            stackAttribution.clear();
        }else{
            
            System.out.println("Erro de tipos { "+stackAttribution.get(underTop).getType()+" := "+stackAttribution.get(top).getType()+" } Linha: " 
                    + currentToken.getLine());
            System.exit(0);
        }
        
    }
    
    private void checkRelational(){
        int top = stackAttribution.size() - 1;
        int underTop = top - 1;
        
        setAttributionType();
        
        if(stackAttribution.get(underTop).getType().equals("real") &&
           (stackAttribution.get(top).getType().equals("integer") || 
            stackAttribution.get(top).getType().equals("real"))){
            
            stackAttribution.remove(top);
            stackAttribution.remove(underTop);
            stackAttribution.add(new IdentifierType("unnamed", "boolean"));
            
        }else if(stackAttribution.get(underTop).getType().equals("integer") &&
                 (stackAttribution.get(top).getType().equals("integer") || 
                  stackAttribution.get(top).getType().equals("real"))){
            
            stackAttribution.remove(top);
            stackAttribution.remove(underTop);
            stackAttribution.add(new IdentifierType("unnamed", "boolean"));
            
        }else{
            
            System.out.println("Erro de tipos (Relacional) { "+stackAttribution.get(underTop).getType()+" "+stackAttribution.get(top).getType()+" } Linha: " 
                    + currentToken.getLine());
            System.exit(0);
        }
        
    }
    
    private void checkArithmetic(){
        int top = stackAttribution.size() - 1;
        int underTop = top - 1;
        
        setAttributionType();
        
        if(opArithmetic.equals("and") || opArithmetic.equals("or")){
            if(stackAttribution.get(underTop).getType().equals("boolean") &&
               stackAttribution.get(top).getType().equals("boolean")){
                
                stackAttribution.remove(top);
            }else{
                
                System.out.println("Erro de tipo (and || or) { "+stackAttribution.get(underTop).getType()+" "+stackAttribution.get(top).getType()+" } Linha: " 
                    + currentToken.getLine());
                System.exit(0);
            }
        }else{
            if(stackAttribution.get(underTop).getType().equals("integer") &&
               stackAttribution.get(top).getType().equals("integer")){
                
                stackAttribution.remove(top);
            }else if(stackAttribution.get(underTop).getType().equals("integer") &&
                     stackAttribution.get(top).getType().equals("real")){
                
                //aqui eu removo o penultimo elemento pra deixar o tipo real como topo
                stackAttribution.remove(underTop);
            }else if(stackAttribution.get(underTop).getType().equals("real") &&
                     (stackAttribution.get(top).getType().equals("integer") || 
                      stackAttribution.get(top).getType().equals("real"))){
                
                stackAttribution.remove(top);
            }else{
                
                System.out.println("Erro tipo (Aritmético) na linha: " + currentToken.getLine() + "{boolean encontrado}");
                System.exit(0);
            }
        }
    }
    
    /*
    Aqui pega o boolean da condição(if ou while) e compara com a expressão
    */
    private void checkConditional(){
        int top = stackAttribution.size() - 1;
        int underTop = top - 1;
        
        setAttributionType();
        
        if(stackAttribution.get(underTop).getType().equals("boolean") &&
           stackAttribution.get(top).getType().equals("boolean")){
            
            stackAttribution.remove(top);
            stackAttribution.remove(underTop);
        }else{
            
            System.out.println("Erro tipo (Condicional): era pra ser dois booleans!!" +" } Linha: " 
                    + currentToken.getLine());
        }
    }
    
    void printDeclaredVariables(){
        for(int i = 0; i < symbolTable.size(); i++){
            System.out.println(symbolTable.get(i).getIdentifier() + " "+symbolTable.get(i).getType());
        }
        System.out.println("");
    }
    
    void checkDeclaration(int type){
        
        if(type == 0){
            for(int i = 0; i < symbolTable.size(); i++){
                if(currentToken.getToken().equals(symbolTable.get(i).getIdentifier())){
                    return;
                }
            }
            System.out.println("{" + currentToken.getToken() + "} undeclared. Line: "+ currentToken.getLine());
            System.exit(0);    
        }else if(type == 1){
            for(int i = 0; i < symbolTableProcedure.size(); i++){
                if(currentToken.getToken().equals(symbolTableProcedure.get(i).getIdentifier())){
                    return;
                }
            }
            System.out.println("{" + currentToken.getToken() + "} undeclared. Line: "+ currentToken.getLine());
            System.exit(0);
        }
        
        
    }
    
    void declaration(int type){
        IdentifierType pair = new IdentifierType(currentToken.getToken(), "undefined");
        //System.out.println("Declaring: " + currentToken.getToken());
        if(type == 0){
            for(int i = symbolTable.size() - 1; i >= 0; i--){
                if(symbolTable.get(i).getIdentifier().equals("$")){
                    symbolTable.add(pair);
                    break;
                }else if(symbolTable.get(i).getIdentifier().equals(currentToken.getToken())){
                    System.out.println("{"+currentToken.getToken() + "} already declared!"+" Linha: "  + currentToken.getLine());
                    System.exit(0);
                    break;
                }
            }
        }else if(type == 1){
            
            for(int i = symbolTableProcedure.size() - 1; i >= 0; i--){
                if(symbolTableProcedure.get(i).getIdentifier().equals("$")){
                    symbolTableProcedure.add(pair);
                    break;
                }else if(symbolTableProcedure.get(i).getIdentifier().equals(currentToken.getToken())){
                    System.out.println("{"+currentToken.getToken() + "} already declared!"+" Linha: "  + currentToken.getLine());
                    System.exit(0);
                    break;
                }
            }
            
        }
          
        //printDeclaredVariables();
    }
    
    public void enterScope(){
        
        IdentifierType pair = new IdentifierType("$", "scope identifier");
        symbolTable.add(pair);
        symbolTableProcedure.add(pair);
    }
    
    public void exitScope(){

        for(int i = symbolTable.size() - 1; i >= 0; i--){
            if(symbolTable.get(i).getIdentifier().equals("$")){
                symbolTable.remove(i);
                break;
            }else
                symbolTable.remove(i);
        }
        for(int i = symbolTableProcedure.size() - 1; i >= 0; i--){
            if(symbolTableProcedure.get(i).getIdentifier().equals("$")){
                symbolTableProcedure.remove(i);
                break;
            }else
                symbolTableProcedure.remove(i);
        }
    }
    
    private void nextToken(){
         if(nextTokenIndex + 1 <= tokens.size())
             this.currentToken = tokens.get(nextTokenIndex++);
             
         if(this.showTokens)
             System.out.println("TOKEN ATUAL: | " + this.currentToken.getToken()
                                + " | CLASSIFICACAO: " + this.currentToken.getClassificacao());
    }
    
    private void previousToken(){
         if(nextTokenIndex - 1 >= 0)
             this.currentToken = tokens.get(--nextTokenIndex);
             
         if(this.showTokens)
             System.out.println("TOKEN ATUAL: | " + this.currentToken.getToken()
                                + " | CLASSIFICACAO: " + this.currentToken.getClassificacao());
    }
    
    public void execute(){

        if(this.tokens == null)
            System.exit(0);

        nextToken();
        if(currentToken.getToken().equals("program")){
            enterScope();
            nextToken();
            if(currentToken.getClassificacao().equals("Identificador")){
                declaration(0);
                nextToken();
                if(currentToken.getToken().equals(";"))
                    nextToken();
                else
                    System.out.println("Esperando ; ao fim do identificador de programa." + " Linha: "  + currentToken.getLine());
                
            }else
                System.out.println("Esperando identificador de programa"+ " Linha: "  + currentToken.getLine());
            
        }else{
            System.out.println("Esperando palavra reservada \" program \""+ " Linha: "  + currentToken.getLine());
        }
        
        if(declaracaoVariaveis()){
            if(declaracaoSubprogramas()){
                if(comandoComposto())
                    if(currentToken.getToken().equals(".")){
                        System.out.println("Deu tudo certo!!");
                    }else
                        System.out.println("Faltou o . no final do programa"+ " Linha: "  + currentToken.getLine());
               else
                    System.out.println("Erro comandoComposto no escopo principal"+ " Linha: "  + currentToken.getLine());
            }else
                System.out.println("Erro declaracaoSubprogramas no escopo principal"+ " Linha: "  + currentToken.getLine());

        }else
            System.out.println("Erro declaracaoVariaveis no escopo principal"+ " Linha: "  + currentToken.getLine());
        
    }

    public boolean declaracaoVariaveis(){

        if(this.currentToken.getToken().equals("var")){
            nextToken();
            return listaDeclaracaoVariaveis();
        }else{
            return true;
        }
    }
    
    public boolean listaDeclaracaoVariaveis_(){
        // TA F0D4
        if(listaIdentificadores()){
            if(currentToken.getToken().equals(":")){
                nextToken();
                if(currentToken.getToken().equals("integer") ||
                   currentToken.getToken().equals("real") ||
                   currentToken.getToken().equals("boolean")){
                    setSymbolType_();
                    variableDeclaration.clear();
                    nextToken();
                    if(currentToken.getToken().equals(";")){
                        nextToken();
                        if(listaDeclaracaoVariaveis_()){
                            return true;
                        }else{
                            System.out.println("Esperando listaDeclaracaoVariaveis_ em listaDeclaracaoVariaveis_"+ " Linha: "  + currentToken.getLine());
                            return false;
                        }
                        
                    }else{
                        System.out.println("Erro, esperando listaDeclaracaoVariaveis_"+ " Linha: "  + currentToken.getLine());
                        return false;
                    }
                }else{
                    System.out.println("Erro esperando tipo em listaDeclaracaoVariaveis_"+ " Linha: "  + currentToken.getLine());
                    return false;
                }
            }else{
                System.out.println("Esperando : em listaDeclaracaoVariaveis_"+ " Linha: "  + currentToken.getLine());
                return false;
            }
        }else{
            return true;
        }
    }
    
    public boolean listaDeclaracaoVariaveis(){
        // TA F0D4
        
        if(listaIdentificadores()){
            if(currentToken.getToken().equals(":")){
                nextToken();
                if(currentToken.getToken().equals("integer") ||
                   currentToken.getToken().equals("real") ||
                   currentToken.getToken().equals("boolean")){
                    
                    setSymbolType_();
                    variableDeclaration.clear();
                    
                    nextToken();
                    if(currentToken.getToken().equals(";")){
                        nextToken();
                        if(listaDeclaracaoVariaveis_()){
                            return true;
                        }else{
                            System.out.println("Esperando listaDeclaracaoVariaveis_ em listaDeclaracaoVariaveis"+ " Linha: "  + currentToken.getLine());
                            return false;
                        }
                        
                    }else{
                        System.out.println("Erro, esperando listaDeclaracaoVariaveis_"+ " Linha: "  + currentToken.getLine());
                        return false;
                    }
                }else{
                    System.out.println("Erro esperando tipo em listaDeclaracaoVariaveis"+ " Linha: "  + currentToken.getLine());
                    return false;
                }
            }else{
                System.out.println("Esperando : em listaDeclaracaoVariaveis"+ " Linha: "  + currentToken.getLine());
                return false;
            }
        }else{
            System.out.println("Esperando lista de identificadores em listaDeclaracaoVariaveis"+ " Linha: "  + currentToken.getLine());
            return false;
        }
    }
    
    public boolean listaIdentificadores_(){

        if(currentToken.getToken().equals(",")){
            nextToken();
            if(currentToken.getClassificacao().equals("Identificador")){
                declaration(0);
                variableDeclaration.add(new IdentifierType(currentToken.getToken(), "unsigned"));
                nextToken();
                if(listaIdentificadores_()){
                    return true;
                }else{
                    System.out.println("Esperando listaIdentificadores_ em listaIdentificadores_"+ " Linha: "  + currentToken.getLine());
                    return false;
                }
            }else{
                System.out.println("Esperando identificador em listaIdentificadores_"+ " Linha: "  + currentToken.getLine());
                return false;
            }
        }else{
            return true;
        }
    }

    public boolean listaIdentificadores(){
        if(currentToken.getClassificacao().equals("Identificador")){
            declaration(0);
            variableDeclaration.add(new IdentifierType(currentToken.getToken(), "unsigned"));
            nextToken();
            if(listaIdentificadores_()){
                return true;
            }else{
                System.out.println("Esperando listaIdentificadores_() em listaIdentificadores"+ " Linha: "  + currentToken.getLine());
                return false;
            }
        }else{
            return false;
        }

    }

   public boolean tipo(){
        if(currentToken.getClassificacao().equals("Palavra reservada") &&
          (currentToken.getToken().equals("integer") || currentToken.getToken().equals("real")
        || currentToken.getToken().equals("boolean"))){
           setSymbolType_();
           nextToken();
           return true;
           
        }
            
        System.out.println("DEU ERRO NO TIPO");
        return false;

   }   
    
   public boolean declaracaoSubprogramas(){
       
       if(declaracaoSubprogramas_()){
           return true;
       }
       return true;
       
   }
   
   public boolean declaracaoSubprogramas_(){
       
       if(declaracaoSubprograma()){
           if(currentToken.getToken().equals(";")){
               nextToken();
               return declaracaoSubprogramas_();
           }else{
               return false;
           }
       }
       
       return true;
   }
   
   public boolean declaracaoSubprograma(){
       
       if(currentToken.getToken().equals("procedure")){
           
           nextToken();
           if(currentToken.getClassificacao().equals("Identificador")){
               
               this.pType.add(new ParameterType(currentToken.getToken()));
                
                declaration(1);
                enterScope();
                nextToken();
                if(argumentos()){
                    indexPType++;
                    if(currentToken.getToken().equals(";")){
                        nextToken();
                        if(declaracaoVariaveis()){
                            if(declaracaoSubprogramas()){
                                
                                if(comandoComposto()){
                                    
                                    exitScope();
                                    return true;
                                }else{
                                    System.out.println("Erro comandoComposto em declaracaoSubprograma"+ " Linha: "  + currentToken.getLine());
                                    return false;
                                }    
                            }else{
                                System.out.println("Erro declaracaoSubprogramas em declaracaoVariaveis"+ " Linha: "  + currentToken.getLine());
                                return false;
                            }
                        }
                    }else{
                        System.out.println("Erro ; em argumentos");
                        return false;
                    } 
               }else{
                   System.out.println("Tem alguma coisa errada nos argumentos!!");
                   return false;
               }
           }else{
               System.out.println("Faltou o identificador, moral!!");
               return false;
           }
       }
       return false;
       
   }

   public boolean argumentos(){

       if(currentToken.getToken().equals("(")){
           nextToken();
           if(listaParametros()){
               if(currentToken.getToken().equals(")")){
                   ArrayList<IdentifierType> aux = (ArrayList<IdentifierType>) variableDeclaration.clone();
                   this.pType.get(indexPType).setList( aux );
                   nextToken();
                   return true;
               }else{
                   System.out.println("Faltou ) nos argumentos"+ " Linha: "  + currentToken.getLine());
                   System.exit(0);
                   return false;
               }
           }else{
               System.out.println("Erro na listaParametros nos argumentos"+ " Linha: "  + currentToken.getLine());
               return false;
           }
       }
       
       return true;
       
   }

   public boolean listaParametros(){

       if(listaIdentificadores()){
           if(currentToken.getToken().equals(":")){
               nextToken();
               if(tipo()){
                   return listaParametros_();
               }else{
                   System.out.println("Erro tipo na listaParametros"+ " Linha: "  + currentToken.getLine());
                   return false;
               }
           }else{
               System.out.println("Erro : na listaParametros"+ " Linha: "  + currentToken.getLine());
               return false;
           }
       }
       
       System.out.println("Erro listaIdentificadoes em listaParametros"+ " Linha: "  + currentToken.getLine());
       return false;
       
   }
   
   public boolean listaParametros_(){
       
       if(currentToken.getToken().equals(";")){
           nextToken();
            if(listaIdentificadores()){
                if(currentToken.getToken().equals(":")){
                    nextToken();
                    if(tipo()){
                        return listaParametros_();
                    }else{
                        System.out.println("Erro tipo na listaParametros_"+ " Linha: "  + currentToken.getLine());
                        return false;
                    }
                }else{
                    System.out.println("Erro : na listaParametros_"+ " Linha: "  + currentToken.getLine());
                    return false;
                }
            }else{
                System.out.println("Erro listaIdentificador em listaParametros_"+ " Linha: "  + currentToken.getLine());
                return false;
            }
       }
       return true;
   }

   public boolean comandoComposto(){
        if (currentToken.getToken().equals("begin")){
           nextToken();
           comandosOpcionais();
           if(currentToken.getToken().equals("end")){
               
               nextToken();
               return true;
            }else{
                System.out.println("Deu pau! esperando end"+ " Linha: "  + currentToken.getLine());
                return false;
            }
        }else{
            return false;
        }
        
   }

    public boolean comandosOpcionais(){
        
        //Caso não tenha nada
        if(currentToken.getToken().equals("end"))
            return true;
        
        if(listaComandos()){
            return true;
        }else{
            return false;
        }
    }
    
    public boolean listaComandos(){
        return comando() && listaComandos_();
    }
    
    public boolean listaComandos_(){
        
        if(currentToken.getToken().equals(";")){
            nextToken();
            if(comando())
                if(listaComandos_()){
                    return true;
                }else{
                    System.out.println("Erro listaComandos_ em listaComandos_"+ " Linha: "  + currentToken.getLine());
                    return false;
                }
        }
        return true;
    }

    public boolean comando(){
       
        if(variavel()){
            if(currentToken.getClassificacao().equals("Atribuição")){
                previousToken();
                previousToken();
                checkDeclaration(0);
                nextToken();
                nextToken();
                nextToken();
                if(expressao()){
                    checkAttribution();
                    return true;
                }else{
                    System.out.println("Erro na expressão em comando"+ " Linha: "  + currentToken.getLine());
                    return false;
                }
            }else if(ativacaoProcedimento()){
                
                return true;
            }else{
                System.out.println("Erro na atribuição em comando"+ " Linha: "  + currentToken.getLine());
                return false;
            }
        }else if(currentToken.getToken().equals("if")){
            stackAttribution.add(new IdentifierType("unnamed", "boolean"));
            nextToken();
            if(expressao()){
                checkConditional();
                if(currentToken.getToken().equals("then")){
                    nextToken();
                    if(comando()){
                        if(parteElse()){
                            nextToken();
                            return true;
                        }else{
                            System.out.println("Erro no else em comando"+ " Linha: "  + currentToken.getLine());
                            return false;
                        }
                    }else{
                        System.out.println("Erro comando em comando"+ " Linha: "  + currentToken.getLine());
                        return false;
                    }
                }else{
                    System.out.println("Erro then em comando"+ " Linha: "  + currentToken.getLine());
                    return false;
                }
            }else{
                System.out.println("Erro expressao if em comando"+ " Linha: "  + currentToken.getLine());
                return false;
            }
        }else if(currentToken.getToken().equals("while")){
            stackAttribution.add(new IdentifierType("unnamed", "boolean"));
            nextToken();
            if(expressao()){
                checkConditional();
                if(currentToken.getToken().equals("do")){
                    nextToken();
                    if(comando()){
                        return true;
                    }else{
                        System.out.println("Erro comando while em comando"+ " Linha: "  + currentToken.getLine());
                        return false;
                    }
                }else{
                    System.out.println("Erro do em comando"+ " Linha: "  + currentToken.getLine());
                    return false;
                }
            }else{
                System.out.println("Erro expressao while em comando"+ " Linha: "  + currentToken.getLine());
                return false;
            }
        }else if(currentToken.getToken().equals("do")){
            nextToken();
            if(comando()){
                if(currentToken.getToken().equals("while")){
                    stackAttribution.add(new IdentifierType("unnamed", "boolean"));
                    nextToken();
                    if(expressao()){
                        checkConditional();
                        return true;
                    }else{
                        System.out.println("Esperando expressao"+ " Linha: "  + currentToken.getLine());
                        return false;
                    }
                }else{
                    System.out.println("Esperando while"+ " Linha: "  + currentToken.getLine());
                    return false;
                }
            }else{
                System.out.println("Esperando comando"+ " Linha: "  + currentToken.getLine());
                return false;
            }
        }
        
        if(comandoComposto()){
            return true;
        }else{
            return false;
        }
    }

    public boolean parteElse(){
        
        if(currentToken.getToken().equals("else")){
            return comando();
        }
        
        return true;
    }

    public boolean variavel(){
        if(currentToken.getClassificacao().equals("Identificador")){
            //checkDeclaration(0);
            stackAttribution.add(new IdentifierType(currentToken.getToken(), "unsigned"));
            nextToken();
            return true;
        }else{
            return false;
        }
    }

    public boolean ativacaoProcedimento(){
        previousToken();
        previousToken();
        checkDeclaration(1);
        int indexpt = 0;
        pList = new ArrayList<>();
        
        for(int i = 0; i < pType.size(); i++){
            if(currentToken.getToken().equals(pType.get(i).getName())){
                indexpt = i;
                break;
            }
        }
        
        nextToken();
        nextToken();
        
        if(currentToken.getToken().equals("(")){
            nextToken();
            if(listaExpressoes()){
                if(currentToken.getToken().equals(")")){
                    
                    if(!pType.get(indexpt).search(pList)){
                        System.exit(0);
                    }

                    nextToken();
                    return true;
                }else{
                    System.out.println("Esperando ) em ativacao procedimento"+ " Linha: "  + currentToken.getLine());
                    System.exit(0);
                    return false;
                }
            }else{
                System.out.println("Esperando listaExpressoes em ativacaoProcedimento"+ " Linha: "  + currentToken.getLine());
                return false;
            }
        }else{
            return true;
        }
        
    }
    
    public boolean listaExpressoes_(){
        
        if(currentToken.getToken().equals(",")){
            nextToken();
            if(expressao()){
                
                if(listaExpressoes_()){
                    return true;
                }
            }else{
                System.out.println("Esperando expressão em listaExpressoes_: " + currentToken.getLine());
            }
                
        }
        return true;
    }

    public boolean listaExpressoes(){
        
        if(expressao()){
            if(listaExpressoes_()){
                return true;
            }
        }
        else{
            System.out.println("Deu pau na lista de expressoes"+ " Linha: "  + currentToken.getLine());
            return false;
        }
        return true;
    }

    public boolean expressao(){

        if(expressaoSimples()){
            if(opRelacional() && expressaoSimples()){
                checkRelational();
                return true;
            }else{
                return true;
            }
        }else{
            return false;
        }
    }

    public boolean expressaoSimples_(){
        
        if(opAditivo()){
            if(termo()){
                checkArithmetic();
                if(expressaoSimples_()){
                    return true;
                }else{
                    System.out.println("deu erro em expressaoSimples_() esperando expressaoSimples_()"+ " Linha: "  + currentToken.getLine());
                    return false;
                }
            }else{
                System.out.println("Esperando termo em expressaoSimples_()"+ " Linha: "  + currentToken.getLine());
                return false;
            }
        }else{
            return true;
        }
    }

    public boolean expressaoSimples(){
        if(termo()){
            if(expressaoSimples_()){
                return true;
            }else{
                System.out.println("Esperando expressaoSimples_() em expressaoSimples()"+ " Linha: "  + currentToken.getLine());
                return false;
            }
        }else if(sinal()){
            if(termo()){
                if(expressaoSimples_()){
                    return true;
                }else{
                    System.out.println("Deu erro em expressao simples, esperando expressaoSimples_"+ " Linha: "  + currentToken.getLine());
                    return false;
                }
            }else{
                System.out.println("Deu erro esperando termo em expressaoSimples()"+ " Linha: "  + currentToken.getLine());
                return false;
            }
        }else{
            return false;
        }

    }

    public boolean termo_(){
       
        if(opMultiplicativo()){
            if(fator()){
                checkArithmetic();
                if(termo_()){
                    return true;
                }else{
                    System.out.println("Deu erro, esperando termo_()"+ " Linha: "  + currentToken.getLine());
                    return false;
                }
            }else{
                System.out.println("Deu erro, esperando fator em termo_()"+ " Linha: "  + currentToken.getLine());
                return false;
            }
        }else{
            return true;
        }
   }
   
    public boolean termo(){
      if(fator() && termo_()){
          return true;
      }else{
          System.out.println("Deu erro no termo.");
          return false;
      }
    }
    
    public boolean fator(){

        if(currentToken.getClassificacao().equals("Número inteiro")){
            stackAttribution.add(new IdentifierType(currentToken.getToken(), "integer"));
            pList.add(new IdentifierType("", "integer"));
            nextToken();
            return true;
        }else if(currentToken.getClassificacao().equals("Número real") ||
                 currentToken.getClassificacao().equals("Real 3D")){
            stackAttribution.add(new IdentifierType(currentToken.getToken(), "real"));
            pList.add(new IdentifierType("", "real"));
            nextToken();
            return true;
        }else if(currentToken.getClassificacao().equals("boolean")){
            stackAttribution.add(new IdentifierType(currentToken.getToken(), "boolean"));
            pList.add(new IdentifierType("", "boolean"));
            nextToken();
            return true;
        }else if(currentToken.getClassificacao().equals("Identificador")){
            checkDeclaration(0);
            stackAttribution.add(new IdentifierType(currentToken.getToken(), "unsigned"));
            
            for(int i = 0; i < symbolTable.size();i++){
                if(currentToken.getToken().equals(symbolTable.get(i).getIdentifier())){
                    pList.add(new IdentifierType("", symbolTable.get(i).getType()));
                    break;
                }
            }
            
            nextToken();
            if(currentToken.getToken().equals("(")){
                if(listaExpressoes()){
                    nextToken();
                    if(currentToken.getToken().equals(")")){
                        nextToken();
                        return true;
                    }else{
                        System.out.println("Deu erro em fator! Esperando )"+ " Linha: "  + currentToken.getLine());
                        return false;
                    }
                }else{
                    System.out.println("Deu pau! Esperando lista de expressoes em fator"+ " Linha: "  + currentToken.getLine());
                    return false;
                }
            }else{
                return true;
            }
            
        }else if(currentToken.getToken().equals("not")){
            nextToken();
            if(fator()){
                nextToken();
                return true;
            }else{
                System.out.println("Deu erro depois do not. Esperando fator"+ " Linha: "  + currentToken.getLine());
                return false;
            }
        }else if(currentToken.getToken().equals("(")){
            nextToken();
            if(expressao()){
                if(currentToken.getToken().equals(")")){
                    nextToken();
                    return true;
                }else{
                    System.out.println("Esperando ) depois da expressao"+ " Linha: "  + currentToken.getLine());
                    return false;
                }
            }else{
                System.out.println("Esperando expressao"+ " Linha: "  + currentToken.getLine());
                return false;
            }

        }else{
            return false;
        }

    }

    public boolean sinal(){
         if(currentToken.getToken().equals("+") || currentToken.getToken().equals("-")){
             nextToken(); 
             return true;
         }
         else{
             System.out.println("Deu pau! esperando sinal + ou -"+ " Linha: "  + currentToken.getLine());
             return false;
         }
    }

    public boolean opRelacional(){
         if(currentToken.getClassificacao().equals("Operador relacional")){
            nextToken();
            return true;
         }    

         return false;
    }

    public boolean opAditivo(){
         if(currentToken.getClassificacao().equals("Operador aditivo")){
            opArithmetic = currentToken.getToken();
            nextToken();
            return true;
         }    
        return false;
    }

    public boolean opMultiplicativo(){
        if(currentToken.getClassificacao().equals("Operador multiplicativo")){
            opArithmetic = currentToken.getToken();
            nextToken();
            return true;
        }    
       return false;
   }
}
