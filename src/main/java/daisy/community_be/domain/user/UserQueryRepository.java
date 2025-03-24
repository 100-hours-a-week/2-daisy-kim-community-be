package daisy.community_be.domain.user;
import static daisy.community_be.domain.user.QUser.user;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

@Repository
public class UserQueryRepository {
    private final JPAQueryFactory queryFactory;

    public UserQueryRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public User findByName(String name) {
        return queryFactory
                .selectFrom(user)
                .where(user.name.eq(name))
                .fetchOne();
    }

    public User findByEmailAndName(String email, String name) {
        return queryFactory
                .selectFrom(user)
                .where(
                        user.email.eq(email),
                        user.name.eq(name)
                )
                .fetchOne();
    }
}




