package AnalisadorLexico;

import java.util.ArrayList;

public class Syntax {
   private ArrayList<Table> tokens;
   private Table currentToken;
   private int nextTokenIndex;
   
    public Syntax(ArrayList<Table> tokens){
        this.tokens = tokens;
        this.nextTokenIndex = 0;
    }

    private void previousToken(){

        if(nextTokenIndex - 1 >= 0)
            this.currentToken = tokens.get(--nextTokenIndex);
        else
            System.out.println("Tem como voltar n, chefe.");
    }

    private void nextToken(){
         if(nextTokenIndex + 1 < tokens.size()){
             this.currentToken = tokens.get(nextTokenIndex++);
             System.out.println("TOKEN ATUAL: " + this.currentToken.getToken()
                                + "CLASSIFICACAO: " + this.currentToken.getClassificacao());
         }
             
         else
             System.out.println("É só isso\n"
                             + "não tem mais jeito\n"
                             + "Acabou\n"
                             + "Boa sorte");
    }

    public void execute(){

        if(this.tokens == null)
            System.exit(0);

        nextToken();

        if(currentToken.getToken().equals("Program")){

            nextToken();

            if(currentToken.getClassificacao().equals("Identificador")){
                nextToken();

                if(currentToken.getToken().equals(";")){
                    nextToken();


                }

            }
        }
    }

    public boolean declaracaoVariaveis(){

        if(this.currentToken.getToken().equals("var")){
            nextToken();
            return listaDeclaracaoVariaveis();
        }

        return false;

    }
    
    public boolean listaDeclaracaoVariaveis_(){
        // TA F0D4
        if(listaIdentificadores()){
            if(currentToken.getToken().equals(":")){
                nextToken();
                if(currentToken.getClassificacao().equals("Número inteiro") ||
                   currentToken.getClassificacao().equals("Número real") ||
                   currentToken.getClassificacao().equals("Real 3D")){
                    nextToken();
                    if(currentToken.getToken().equals(";")){
                        nextToken();
                        if(listaDeclaracaoVariaveis_()){
                            return true;
                        }else{
                            System.out.println("Esperando listaDeclaracaoVariaveis_ em listaDeclaracaoVariaveis");
                            return false;
                        }
                        
                    }else{
                        System.out.println("Erro, esperando listaDeclaracaoVariaveis_");
                        return false;
                    }
                }else{
                    System.out.println("Erro esperando tipo em listaDeclaracaoVariaveis");
                    return false;
                }
            }else{
                System.out.println("Esperando : em listaDeclaracaoVariaveis");
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
                if(currentToken.getClassificacao().equals("Número inteiro") ||
                   currentToken.getClassificacao().equals("Número real") ||
                   currentToken.getClassificacao().equals("Real 3D")){
                    nextToken();
                    if(currentToken.getToken().equals(";")){
                        nextToken();
                        if(listaDeclaracaoVariaveis_()){
                            return true;
                        }else{
                            System.out.println("Esperando listaDeclaracaoVariaveis_ em listaDeclaracaoVariaveis");
                            return false;
                        }
                        
                    }else{
                        System.out.println("Erro, esperando listaDeclaracaoVariaveis_");
                        return false;
                    }
                }else{
                    System.out.println("Erro esperando tipo em listaDeclaracaoVariaveis");
                    return false;
                }
            }else{
                System.out.println("Esperando : em listaDeclaracaoVariaveis");
                return false;
            }
        }else{
            System.out.println("Esperando lista de identificadores em listaDeclaracaoVariaveis");
            return false;
        }
    }
    
    public boolean listaIdentificadores_(){

        if(currentToken.getToken().equals(",")){
            nextToken();
            if(currentToken.getClassificacao().equals("Identificador")){
                nextToken();
                if(listaIdentificadores_()){
                    nextToken();
                    return true;
                }else{
                    System.out.println("Esperando listaIdentificadores_ em listaIdentificadores_");
                    return false;
                }
                    
            }else{
                System.out.println("Esperando identificador em listaIdentificadores_");
                return false;
            }
        }else{
            return true;
        }
    }

    public boolean listaIdentificadores(){
        if(currentToken.getClassificacao().equals("Identificador")){
            nextToken();
            if(listaIdentificadores_()){
                return true;
            }else{
                System.out.println("Esperando listaIdentificadores_() em listaIdentificadores");
                return false;
            }
        }

        return true;
    }

   public boolean tipo(){
        if(currentToken.getClassificacao().equals("Palavra reservada") &&
          (currentToken.getToken().equals("integer") || currentToken.getToken().equals("real")
        || currentToken.getToken().equals("boolean"))){
           
           nextToken();
           return true;
           
        }
            
        System.out.println("DEU ERRO NO TIPO");
        return false;

   }   
    
   public boolean declaracaoSubprogramas(){
       
       return declaracaoSubprogramas_();
       
   }
   
   public boolean declaracaoSubprogramas_(){
       
       if(declaracaoSubprograma()){
           
           nextToken();
           
           if(currentToken.getToken().equals(";")){
               
               nextToken();
               return declaracaoSubprogramas_();
               
           }else{
               
               System.out.println("Faltou o ; em declaracaoSubprogramas_()");
               return false;
               
           }
           
       }
       
       return true;
   }
   
   public boolean declaracaoSubprograma(){
       
       if(currentToken.getToken().equals("procedure")){
           
           nextToken();
           
           if(currentToken.getClassificacao().equals("Identificador")){
               
                nextToken();
               
                if(argumentos()){
                   
                   nextToken();
                   
                    if(currentToken.getToken().equals(";")){
                       
                        nextToken();
                       
                        if(declaracaoVariaveis()){

                            nextToken();

                            if(declaracaoSubprogramas()){

                                nextToken();

                                if(comandoComposto()){

                                    nextToken();
                                    return true;

                                }else{

                                    System.out.println("Erro comandoComposto em declaracaoSubprograma");
                                    return false;

                                }    


                            }else{

                                System.out.println("Erro declaracaoSubprogramas em declaracaoVariaveis");
                                return false;

                            }

                        }else{
                            
                            System.out.println("Erro declaracaoVariaveis em ;");
                            return false;
                            
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
               
               nextToken();
               
               if(currentToken.getToken().equals(")")){
                   
                   nextToken();
                   return true;
                   
               }else{
                   
                   System.out.println("Faltou ) nos argumentos");
                   return false;
                   
               }
               
           }else{
               
               System.out.println("Erro na listaParametros nos argumentos");
               return false;
               
           }
           
       }
       
       return true;
       
   }

   
   /*
        ########################################
        Falta fazer
        ########################################
    
   */
   public boolean listaParametros(){
       
       if(listaIdentificadores()){
           
           nextToken();
           
           if(currentToken.getToken().equals(":")){
               
               nextToken();
               
               if(tipo()){
                   
                   nextToken();
                   return true;
                   
               }else{
                   
                   System.out.println("Erro tipo na listaParametros");
                   return false;
                   
               }
               
           }else{
               
               System.out.println("Erro : na listaParametros");
               return false;
               
           }
           
       }else{
           
           
           
       }
       
       return false;
       
   }

   public boolean comandoComposto(){
        if (currentToken.getToken().equals("begin")){
           nextToken();
           comandosOpcionais();
           if(currentToken.getToken().equals("end")){
               nextToken();
               return true;
            }else{
                System.out.println("Deu pau! esperando end");
                return false;
            }
        }else{
            System.out.println("DEU PAU! esperando begin");
            return false;
        }
        
   }

    public void comandosOpcionais(){


    }

    public void listaComandos(){

    }

    public void comando(){


    }

    public void parteElse(){

    }

    public void variavel(){
        if(currentToken.getClassificacao().equals("Identificador")){
            nextToken();
        }else
            System.out.println("DEU PAU NA VARIAVEL! Esperando Identificador.");
    }

    public boolean ativacaoProcedimento(){
        if(currentToken.getClassificacao().equals("Identificador")){
            nextToken();
            if(currentToken.getToken().equals("(")){
                nextToken();
                if(listaExpressoes()){
                    if(currentToken.getToken().equals(")")){
                        nextToken();
                        return true;
                    }else{
                        System.out.println("Esperando ) em ativacao procedimento");
                        return false;
                    }
                }else{
                    System.out.println("Esperando listaExpressoes em ativacaoProcedimento");
                    return false;
                }
            }else{
                return true;
            }
        }else{
            System.out.println("Esperando identificador em ativacaoProcedimento");
            return false;
        }
    }

    public boolean listaExpressoes(){
        nextToken();
        if(expressao()){
            nextToken();
        }else if(listaExpressoes()){
            nextToken();
        }else{
            System.out.println("Deu pau na lista de expressoes");
            return false;
        }
        return true;
    }

    public boolean expressao(){

        if(expressaoSimples()){
            if(opRelacional() && expressaoSimples()){
                //nextToken();
                return true;
            }else{
                nextToken();
                return true;
            }
        }else{
            return false;
        }
    }

    public boolean expressaoSimples_(){
        
        if(opAditivo()){
            if(termo()){
                if(expressaoSimples_()){
                    nextToken();
                    return true;
                }else{
                    System.out.println("deu erro em expressaoSimples_() esperando expressaoSimples_()");
                    return false;
                }
            }else{
                System.out.println("Esperando termo em expressaoSimples_()");
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
                System.out.println("Esperando expressaoSimples_() em expressaoSimples()");
                return false;
            }
        }else if(sinal()){
            if(termo()){
                if(expressaoSimples_()){
                    return true;
                }else{
                    System.out.println("Deu erro em expressao simples, esperando expressaoSimples_");
                    return false;
                }
            }else{
                System.out.println("Deu erro esperando termo em expressaoSimples()");
                return false;
            }
        }else{
            return false;
        }

    }

    public boolean termo_(){
       
        if(opMultiplicativo()){
            if(fator()){
                if(termo_()){
                    nextToken();
                    return true;
                }else{
                    System.out.println("Deu erro, esperando termo_()");
                    return false;
                }
            }else{
                System.out.println("Deu erro, esperando fator em termo_()");
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


        /*
             +++++++++++++++++++++++++++++++++++++++
             FALTANDO TRATAR O ''' TRUE E FALSE '''
             +++++++++++++++++++++++++++++++++++++++
        */
        if(currentToken.getClassificacao().equals("Número inteiro") ||
           currentToken.getClassificacao().equals("Número real") ||
           currentToken.getClassificacao().equals("Real 3D")){
            nextToken();
            return true;
        }else if(currentToken.getClassificacao().equals("Identificador")){
            nextToken();
            if(currentToken.getToken().equals("(")){
                if(listaExpressoes()){
                    nextToken();
                    if(currentToken.getToken().equals(")")){
                        nextToken();
                        return true;
                    }else{
                        System.out.println("Deu erro em fator! Esperando )");
                        return false;
                    }
                }else{
                    System.out.println("Deu pau! Esperando lista de expressoes em fator");
                    return false;
                }
            }
            return true;
        }else if(currentToken.getToken().equals("not")){
            nextToken();
            if(fator()){
                nextToken();
                return true;
            }else{
                System.out.println("Deu erro depois do not. Esperando fator");
                return false;
            }
        }else if(currentToken.getToken().equals("(")){
            nextToken();
            if(expressao()){
                nextToken();
                if(currentToken.getToken().equals(")")){
                    nextToken();
                    return true;
                }else{
                    System.out.println("Esperando ) depois da expressao");
                    return false;
                }
            }else{
                System.out.println("Esperando expressao");
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
             System.out.println("Deu pau! esperando sinal + ou -");
             return false;
         }
    }

    public boolean opRelacional(){
         if(currentToken.getToken().equals("Operador relacional")){
            nextToken();
            return true;
         }    
         else{
             previousToken();
             System.out.println("Deu pau! esperando operador relacional");
         }

         return false;
    }

    public boolean opAditivo(){
         if(currentToken.getToken().equals("Operador aditivo")){
            nextToken();
            return true;
         }    
         else{
             System.out.println("Deu pau! esperando operador aditivo");
             previousToken();
         }
        return false;
    }

    public boolean opMultiplicativo(){
        if(currentToken.getToken().equals("Operador multiplicativo")){
           nextToken();
           return true;
        }    
        else{
            System.out.println("Deu pau! esperando operador multiplicativo");
        }

       return false;
   }
}
