# 3a)
# open first connection
# set isolation to RR and disable Auto-commit in \
#	case this is used in a new session, after exercise 2
db2 connect vsisp user vsisp09
#: first connection:
db2 set isolation = RR
db2options='+c'

# opening second connection in another ssh-session
db2 connect vsisp user vsisp09
#: second connection
db2options='+c'

#: first connection:
db2 "select * from OPK where id == 1"
#: second connection:
db2 "select * from OPK where id == 2"
db2 "update OPK name = 'Hannelore' where id == 1"

#: first connection:
db2 "update OPK name = 'Sieglinde' where id == 2"

# connection 1 locks on id=1 but cann update id=2, cause of the cs-restriction\
#	in connection 2. commiting connection 2 after conenction 1 should solve it


# 3b)

# only difference in code for second connection:
#: second connection
db2 set isolation = RS

# in this case a deadlock occurs. 