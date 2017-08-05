import os
import pandas as pd
import numpy as np
import datetime
import matplotlib.pyplot as plt

# "identificador":"51168250999992015",
# "uasg":511682,
# "modalidade_licitacao":7,
# "numero_aviso_licitacao":22014,
# "codigo_contrato":50,
# "licitacao_associada":"51168207000022014",
# "origem_licitacao":"SISPP",
# "numero":999992015,
# "objeto":"Fornecimento de \u001Agua e esgoto para a Ge r\u001Ancia Executiva do INSS em Feira de Santana e unidades jurisdicionadas.",
# "numero_processo":"35025000317201456",
# "cnpj_contratada":"13504675000110",
# "data_assinatura":"2015-01-19",
# "fundamento_legal":"Lei 8666/93",
# "data_inicio_vigencia":"2015-01-19",
# "data_termino_vigencia":"2099-12-31",
# "valor_inicial":157224.36

## carregando cada arquivo
colunas = ['identificador', 'uasg', 'modalidade_licitacao', 'numero_aviso_licitacao', 'codigo_contrato',
		   'licitacao_associada', 'origem_licitacao', 'numero', 'objeto', 'numero_processo', 'cnpj_contratada',
		   'data_assinatura', 'fundamento_legal', 'data_inicio_vigencia', 'data_termino_vigencia', 'valor_inicial'] 

df1 = pd.read_csv('/Users/user/projects/HackathonFiesp2017/data/test_contract.json', encoding='utf-8', names=colunas, skiprows=1)
# df2 = pd.read_csv('/Users/user/projects/HackathonFiesp2017/data/contratos.json', encoding='utf-8', names=colunas, skiprows=1)