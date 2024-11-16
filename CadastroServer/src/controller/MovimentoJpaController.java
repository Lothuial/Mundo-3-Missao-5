/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package controller;

import controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import model.Movimento;
import model.Pessoa;
import model.Produto;
import model.Usuario;


/**
 *
 * @author fel-f
 */

public class MovimentoJpaController implements Serializable {
    
    public MovimentoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Movimento movimento) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoa idPessoa = movimento.getFKPessoaid();
            if (idPessoa != null) {
                idPessoa = em.getReference(idPessoa.getClass(), idPessoa.getIdPessoa());
                movimento.setFKPessoaid(idPessoa);
            }
            Produto idProduto = movimento.getFKProdutoid();
            if (idProduto != null) {
                idProduto = em.getReference(idProduto.getClass(), idProduto.getIdProduto());
                movimento.setFKProdutoid(idProduto);
            }
            Usuario idUsuario = movimento.getFKUsuarioid();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getIdUsuario());
                movimento.setFKUsuarioid(idUsuario);
            }
            em.persist(movimento);
            if (idPessoa != null) {
                idPessoa.getMovimentoCollection().add(movimento);
                idPessoa = em.merge(idPessoa);
            }
            if (idProduto != null) {
                idProduto.getMovimentoCollection().add(movimento);
                idProduto = em.merge(idProduto);
            }
            if (idUsuario != null) {
                idUsuario.getMovimentoCollection().add(movimento);
                idUsuario = em.merge(idUsuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Movimento movimento) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Movimento persistentMovimento = em.find(Movimento.class, movimento.getIdMovimento());
            Pessoa idPessoaOld = persistentMovimento.getFKPessoaid();
            Pessoa idPessoaNew = movimento.getFKPessoaid();
            Produto idProdutoOld = persistentMovimento.getFKProdutoid();
            Produto idProdutoNew = movimento.getFKProdutoid();
            Usuario idUsuarioOld = persistentMovimento.getFKUsuarioid();
            Usuario idUsuarioNew = movimento.getFKUsuarioid();
            if (idPessoaNew != null) {
                idPessoaNew = em.getReference(idPessoaNew.getClass(), idPessoaNew.getIdPessoa());
                movimento.setFKPessoaid(idPessoaNew);
            }
            if (idProdutoNew != null) {
                idProdutoNew = em.getReference(idProdutoNew.getClass(), idProdutoNew.getIdProduto());
                movimento.setFKProdutoid(idProdutoNew);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getIdUsuario());
                movimento.setFKUsuarioid(idUsuarioNew);
            }
            movimento = em.merge(movimento);
            if (idPessoaOld != null && !idPessoaOld.equals(idPessoaNew)) {
                idPessoaOld.getMovimentoCollection().remove(movimento);
                idPessoaOld = em.merge(idPessoaOld);
            }
            if (idPessoaNew != null && !idPessoaNew.equals(idPessoaOld)) {
                idPessoaNew.getMovimentoCollection().add(movimento);
                idPessoaNew = em.merge(idPessoaNew);
            }
            if (idProdutoOld != null && !idProdutoOld.equals(idProdutoNew)) {
                idProdutoOld.getMovimentoCollection().remove(movimento);
                idProdutoOld = em.merge(idProdutoOld);
            }
            if (idProdutoNew != null && !idProdutoNew.equals(idProdutoOld)) {
                idProdutoNew.getMovimentoCollection().add(movimento);
                idProdutoNew = em.merge(idProdutoNew);
            }
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getMovimentoCollection().remove(movimento);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getMovimentoCollection().add(movimento);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = movimento.getIdMovimento();
                if (findMovimento(id) == null) {
                    throw new NonexistentEntityException("The movimento with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Movimento movimento;
            try {
                movimento = em.getReference(Movimento.class, id);
                movimento.getIdMovimento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The movimento with id " + id + " no longer exists.", enfe);
            }
            Pessoa idPessoa = movimento.getFKPessoaid();
            if (idPessoa != null) {
                idPessoa.getMovimentoCollection().remove(movimento);
                idPessoa = em.merge(idPessoa);
            }
            Produto idProduto = movimento.getFKProdutoid();
            if (idProduto != null) {
                idProduto.getMovimentoCollection().remove(movimento);
                idProduto = em.merge(idProduto);
            }
            Usuario idUsuario = movimento.getFKUsuarioid();
            if (idUsuario != null) {
                idUsuario.getMovimentoCollection().remove(movimento);
                idUsuario = em.merge(idUsuario);
            }
            em.remove(movimento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Movimento> findMovimentoEntities() {
        return findMovimentoEntities(true, -1, -1);
    }

    public List<Movimento> findMovimentoEntities(int maxResults, int firstResult) {
        return findMovimentoEntities(false, maxResults, firstResult);
    }

    private List<Movimento> findMovimentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Movimento.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Movimento findMovimento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Movimento.class, id);
        } finally {
            em.close();
        }
    }

    public int getMovimentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Movimento> rt = cq.from(Movimento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }   
}