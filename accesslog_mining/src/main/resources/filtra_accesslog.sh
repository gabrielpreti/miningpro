#!/bin/sh

#filtra acessos a index.jhtml entre 17:30 e 21:00
cat /git_repos/github/accesslog_treding_analysis/tests/access_pagseguro.uol.com.br_443.log.201409120200 | grep "GET /pagseguro.uol.com.br/index.jhtml" | grep -e "10/Sep/2014:17:3\|10/Sep/2014:17:4\|10/Sep/2014:17:5\|10/Sep/2014:18\|10/Sep/2014:19\|10/Sep/2014:20"
