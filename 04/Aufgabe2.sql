# 2a)
# open first connection
# set isolation to RS and disable Auto-commit in \
#	case this is used in a new session, after exercise 1
db2 connect vsisp user vsisp09
#: first connection:
db2 set isolation = RS
db2options='+c'
# add some more data for easier working
db2 "insert into  OPK (id, name) values (4, 'Corny')"
db2 "insert into  OPK (id, name) values (5, 'Peter')"
db2 "insert into  OPK (id, name) values (6, 'Frank')"
# query all all id > 3
db2 "select * from OPK where id > 3"

# opening second connection in another ssh-session
db2 connect vsisp user vsisp09
#: second connection
db2 "insert into OPK (id, name) values (7, 'Hans')"

#: first connection
db2 "select * from OPK where id > 3"
db2 commit
# everything works fine, the additional entry is shown


# 2b)
#: first connection
db2 set isolation=RR
db2 "select * from OPK where id > 3"

#: second connection
db2 "insert into OPK (id, name) values (8, 'Gabi')"

#: first connection
db2 "select * from OPK where id > 3"
db2 commit
# only after the commit is done, the insert-transaction can be fullfilled
db2 "select * from OPK where id > 3"
# at this point the entry is shown


# 2c)
#: first connection 
db2 "select * from OPK where id == 7"

#: second connection
# ToDo: hier bin ich mir nicht sicher, ob es tatsaechlich einfach so geht... scheiss doku
db2 "update OPK name = 'Hannelore' WHERE id == 7"

#: first connection
db2 commit
db2 "select * from OPK where id == 7"
# after the commit, the update is visible


# 2d)
#: first connection
db2 "create table MPK (ID integer primary key, Name varchar(50))"
db2 "insert into MPK (id, name) values (1, 'julian')"
db2 "insert into MPK (id, name) values (2, 'Felix')"
db2 "insert into MPK (id, name) values (3, 'Tobias')"
db2 "insert into MPK (id, name) values (4, 'Corny')"
db2 "insert into MPK (id, name) values (5, 'Peter')"
db2 "insert into MPK (id, name) values (6, 'Frank')"
db2 "insert into MPK (id, name) values (7, 'Hans')"
db2 "insert into MPK (id, name) values (8, 'Gabi')"
db2 commit

# 2e)
db2 "select * from MPK where id == 1"

#: second connection
db2 "update MPK name = 'Hannelore' WHERE id == 7"
# update works before the commit

#: first connection 
db2 commit



