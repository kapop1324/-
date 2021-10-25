package com.web.curation.controller.newsfeed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.curation.model.AchieveListFromDB;
import com.web.curation.model.CommentFromDB;
import com.web.curation.model.CommentToClient;
import com.web.curation.model.FeedRecipe;
import com.web.curation.model.HashtagVO;
import com.web.curation.model.Paging;
import com.web.curation.model.RecipeContent;
import com.web.curation.model.RecipeInfo;
import com.web.curation.model.RecipeInfoFromDB;
import com.web.curation.model.RecipeDetailFromDB;
import com.web.curation.model.RecipeDetailToClient;
import com.web.curation.model.SaveComment;
import com.web.curation.model.SaveLikeComment;
import com.web.curation.model.SaveLikeRecipe;
import com.web.curation.model.service.FeedService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.web.curation.model.SaveRecipeContent;
import com.web.curation.model.SaveRecipeitem;
import com.web.curation.model.UpdateComment;
import com.web.curation.model.UpdateRecipeContentToDB;
import com.web.curation.model.UpdateRecipeFromClient;

@CrossOrigin("*")
@RequestMapping("/feed")
@RestController
public class FeedController {

	@Autowired
	FeedService feedService;

	// 글쓰기 기능 (제목, 아이디, 사진, 설명, 썸네일여부) - post
	// 임시 - 제목, 아이디, 설명
	@PostMapping("/write")
	public ResponseEntity<String> writeRecipe(SaveRecipeitem data) throws Exception {
		String id = data.getId();
		String title = data.getTitle();
		String nickname = data.getNickname();
		List<String> imgList = data.getImg();
		List<String> desList = data.getDes();
		List<String> thumbnailList = data.getThumbnail();
		List<String> hashtagList = data.getHashtags();

		String achieve_master = data.getAchieve_master();
		String achieve_slave = data.getAchieve_slave();
		
		if (id == null) {
			return new ResponseEntity<String>("Fail", HttpStatus.BAD_REQUEST);
		}
		if (title == null || imgList.isEmpty() || desList.isEmpty()) {
			return new ResponseEntity<String>("Fail", HttpStatus.NO_CONTENT);
		}

		RecipeInfo recipeInfo = new RecipeInfo();
		recipeInfo.setId(id);
		recipeInfo.setNickname(nickname);
		recipeInfo.setTitle(title);
		recipeInfo.setAchieve_master(achieve_master);
		recipeInfo.setAchieve_slave(achieve_slave);
		
		Map<String, String> map = new HashMap<>();
		map.put("title", title);
		map.put("id", id);

		// recipe_info 저장 (title, id, regdate)
		feedService.writeRecipeInfo(recipeInfo);

		// recipe_idx 가져오기
		int recipe_idx = feedService.getRecipe_idx(map);

		// recipe_content 저장
		for (int i = 0; i < imgList.size(); i++) {
			SaveRecipeContent content = new SaveRecipeContent();
			content.setImg(imgList.get(i));
			content.setDes(desList.get(i));
			content.setThumbnail(thumbnailList.get(i));
			content.setRecipe_idx(recipe_idx);
			content.setContent_order(i);

			feedService.writeRecipeContent(content);
		}
		
		if(hashtagList!=null) {
			int size = hashtagList.size();
			HashMap<String, Object> hash = new HashMap<String, Object>();
			for (int i = 0; i < size; i++) {
				hash.put("recipe_idx", recipe_idx);
				hash.put("hashtag", hashtagList.get(i));
				feedService.writeHashtags(hash);
				
				hash.clear();
			}
		}

		return new ResponseEntity<String>("Success", HttpStatus.OK);
	}

	// liked posts
	@GetMapping("/likedposts")
	public ResponseEntity<Map<String, Object>> getLikedPosts(@RequestParam Map map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String result = "SUCCESS";
		try {

			int listCnt = feedService.getFeedCnt();
			int page = Integer.parseInt((String)map.get("page"));
			Paging paging = new Paging();
			paging.pageInfo(page, (page-1) * paging.getRangeSize(), listCnt);
			map.put("paging", paging);
			List<RecipeContent> list = feedService.getLikedPosts(map);
			for(RecipeContent rc: list) {
				rc.setComment(feedService.getTotalCommentNum(rc.getIdx()));
				rc.setHashtags(feedService.getHashtagList(rc.getIdx()));
			}
			resultMap.put("message", result);
			resultMap.put("latestPosts", list);
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	// 레시피 하나의 내용 보여주기
	@GetMapping("/content")
	public ResponseEntity<Map<String, Object>> recipeDetail(@RequestParam int recipe_idx, String recipe_user_id) {
		String result = "SUCCESS";
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.OK;

		try {
			List<RecipeDetailFromDB> recipeDetailFromDB = feedService.getRecipeContents(recipe_idx);
			List<Integer> commentCount = new ArrayList<>();
			for(int i=0; i<recipeDetailFromDB.size(); i++) {
				int content_idx = recipeDetailFromDB.get(i).getIdx();
				commentCount.add(feedService.getCommentNum(content_idx));
			}
			
			if (recipeDetailFromDB == null) {
				resultMap.put("message", "FAIL");
				return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NO_CONTENT);
			}

			RecipeDetailToClient recipeDetailToClient = new RecipeDetailToClient();
			recipeDetailToClient.setRecipe_contents(recipeDetailFromDB);
			recipeDetailToClient.setCommentCount(commentCount);
			// recipe 정보 : title, id, regdate, nickname
			RecipeInfoFromDB recipeInfoFromDB = feedService.getRecipeInfo(recipe_idx);

			if (recipeInfoFromDB == null) {
				resultMap.put("message", "FAIL");
				return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.BAD_REQUEST);
			}

			// 레시피 기본 정보
			recipeDetailToClient.setTitle(recipeInfoFromDB.getTitle());
			recipeDetailToClient.setRegdate(recipeInfoFromDB.getRegdate());
			recipeDetailToClient.setId(recipeInfoFromDB.getId());
			recipeDetailToClient.setNickname(recipeInfoFromDB.getNickname());
			recipeDetailToClient.setAchieve_master(recipeInfoFromDB.getAchieve_master());
			recipeDetailToClient.setAchieve_slave(recipeInfoFromDB.getAchieve_slave());

			// 좋아요 수
			recipeDetailToClient.setLike(feedService.getLikeCountRecipe(recipe_idx));

			// 사용자가 해당 레시피에 좋아요 눌렀는지 체크
			HashMap<Object, Object> map = new HashMap<>();
			map.put("recipe_user_id", recipe_user_id);
			map.put("recipe_idx", recipe_idx);
			if (feedService.checkLikeRecipe(map) == 1) {
				recipeDetailToClient.setLikecheck(true);
			} else {
				recipeDetailToClient.setLikecheck(false);
			}
			
			//해시태그 받아오기
			List<HashtagVO> hashtagList = feedService.getHashtag(recipe_idx);
			
			List<Integer> hashtag_idx = new ArrayList<>();
			List<String> tag = new ArrayList<>();
			
			for(int i=0; i<hashtagList.size(); i++) {
				int idx = hashtagList.get(i).getIdx();
				String hashtag = hashtagList.get(i).getTag();
				
				hashtag_idx.add(idx);
				tag.add(hashtag);
		
			}
			recipeDetailToClient.setHashtag_idx(hashtag_idx);
			recipeDetailToClient.setTag(tag);
			
			resultMap.put("recipeContent", recipeDetailToClient);

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	// 최신피드 (메인)
	@GetMapping("/latestfeed")
	public ResponseEntity<Map<String, Object>> latestFeed(@RequestParam Map params) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String result = "SUCCESS";
		String id = (String)params.get("id");
		try {
			int listCnt = feedService.getFeedCnt();
			int page = Integer.parseInt((String)params.get("page"));
			Paging paging = new Paging();
			paging.pageInfo(page, (page-1) * paging.getRangeSize(), listCnt);
			params.put("paging", paging);
			List<FeedRecipe> recipe = feedService.getLatestFeed(params);
			for(FeedRecipe rc: recipe) {
				rc.setComment(feedService.getTotalCommentNum(rc.getIdx()));
				rc.setHashtags(feedService.getHashtagList(rc.getIdx()));
			}
			HashMap<Object, Object> map = new HashMap<>();
			map.put("recipe_user_id", id);
			for (int i = 0; i < recipe.size(); i++) {
				int recipe_idx = recipe.get(i).getIdx();
				map.put("recipe_idx", recipe_idx);
				if (feedService.checkLikeRecipe(map) == 1) {
					recipe.get(i).setLikecheck(true);
				} else {
					recipe.get(i).setLikecheck(false);
				}
				recipe.get(i).setLike(feedService.getLikeCountRecipe(recipe_idx));
			}

			resultMap.put("latestFeed", recipe);

			if (recipe == null) {
				result = "FAIL";
			} else {
				result = "SUCCESS";
			}

			resultMap.put("message", result);
			status = HttpStatus.ACCEPTED;

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	// 프로필 - 자기 레시피
	@GetMapping("/mastercount")
	public ResponseEntity<Map<String, Object>> getUserMasterCount(@RequestParam String id) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String result = "SUCCESS";
		try {
			List<Integer> recipe = feedService.getMasterCount(id);

			resultMap.put("latestFeed", recipe);

			if (recipe == null) {
				result = "FAIL";
			} else {
				result = "SUCCESS";
			}

			resultMap.put("message", result);
			status = HttpStatus.ACCEPTED;

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	// 프로필 - 자기 레시피
	@GetMapping("/allrecipes")
	public ResponseEntity<Map<String, Object>> getAllRecipes(@RequestParam String id) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String result = "SUCCESS";
		try {
			List<RecipeContent> recipe = feedService.getAllRecipes(id);
			
			resultMap.put("latestFeed", recipe);

			if (recipe == null) {
				result = "FAIL";
			} else {
				result = "SUCCESS";
			}

			resultMap.put("message", result);
			status = HttpStatus.ACCEPTED;

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	// 댓글 작성
	@PostMapping("/comment/write")
	public ResponseEntity<String> writeComment(@RequestBody SaveComment comment) {
		try {
			if (feedService.writeComment(comment) == 1) {
				
				return new ResponseEntity<String>("Success", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Fail", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Fail", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 댓글 수정
	@PutMapping("/comment/update")
	public ResponseEntity<String> updateComment(@RequestBody UpdateComment comment) {
		try {
			if (feedService.updateComment(comment) == 1) {
				
				return new ResponseEntity<String>("Success", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Fail", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Fail", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 댓글 삭제
	@DeleteMapping("/comment/delete")
	public ResponseEntity<String> deleteComment(@RequestParam int idx) {
		try {
			if (feedService.deleteComment(idx) == 1) {
				
				return new ResponseEntity<String>("Success", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Fail", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Fail", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 댓글 좋아요
	@PostMapping("/comment/like")
	public ResponseEntity<String> LikeComment(@RequestBody SaveLikeComment saveLike) {
		HashMap<Object, Object> map = new HashMap<>();
		map.put("id", saveLike.getId());
		map.put("comment_idx", saveLike.getComment_idx());

		try {
			if (feedService.checkLikeComment(map) > 0) {
				
				return new ResponseEntity<String>("Fail", HttpStatus.BAD_REQUEST);
			}
			if (feedService.likeComment(map) == 1) {
				
				return new ResponseEntity<String>("Success", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Fail", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Fail", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 댓글 좋아요 취소
	@DeleteMapping("/comment/like")
	public ResponseEntity<String> CancelLikeComment(@RequestParam int comment_idx, String id) {
		HashMap<Object, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("comment_idx", comment_idx);

		try {
			if (feedService.cancelLikeComment(map) == 1) {

				return new ResponseEntity<String>("Success", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Fail", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Fail", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 댓글 조회
	@GetMapping("/comment")
	public ResponseEntity<Map<String, Object>> CommentList(@RequestParam int content_idx, String id) {
		Map<String, Object> resultMap = new HashMap<>();
		List<CommentToClient> commentToClient = new ArrayList<>();
		try {
			List<CommentFromDB> commentListFromDB = feedService.getCommentList(content_idx);
			if (commentListFromDB.size() == 0) {
				resultMap.put("message", "no comment");
				return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NO_CONTENT);
			}
			for (int i = 0; i < commentListFromDB.size(); i++) {
				CommentToClient c = new CommentToClient();
				c.setIdx(commentListFromDB.get(i).getIdx());
				c.setComment(commentListFromDB.get(i).getComment());
				c.setRegdate(commentListFromDB.get(i).getRegdate());
				c.setId(commentListFromDB.get(i).getId());
				c.setNickname(commentListFromDB.get(i).getNickname());
				commentToClient.add(c);
			}
			HashMap<Object, Object> map = new HashMap<>();
			map.put("id", id);

			for (int i = 0; i < commentListFromDB.size(); i++) {
				int comment_idx = commentListFromDB.get(i).getIdx();
				commentToClient.get(i).setLike(feedService.getLikeCountComment(comment_idx));
				map.put("comment_idx", comment_idx);
				if (feedService.checkLikeComment(map) == 1) {
					commentToClient.get(i).setLikecheck(true);
				} else {
					commentToClient.get(i).setLikecheck(false);
				}
			}
			resultMap.put("commentList", commentToClient);
			resultMap.put("message", "Success");
		

			return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("message", "Fail");
			return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 레시피 삭제
	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteRecipe(@RequestParam int idx) {

		try {
			if (feedService.deleteRecipe(idx) == 1) {
			
				return new ResponseEntity<String>("Success", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Fail", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Fail", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 레시피 좋아요
	@PostMapping("/like")
	public ResponseEntity<String> LikeRecipe(@RequestBody SaveLikeRecipe saveLike) {
		HashMap<Object, Object> map = new HashMap<>();
		map.put("recipe_user_id", saveLike.getRecipe_user_id());
		map.put("recipe_idx", saveLike.getRecipe_idx());

		try {
			if (feedService.checkLikeRecipe(map) > 0) {
			
				return new ResponseEntity<String>("Fail", HttpStatus.BAD_REQUEST);
			}
			if (feedService.likeRecipe(map) == 1) {
				
				return new ResponseEntity<String>("Success", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Fail", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Fail", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 레시피 좋아요 취소
	@DeleteMapping("/like")
	public ResponseEntity<String> CancelLikeRecipe(@RequestParam int recipe_idx, String recipe_user_id) {
		HashMap<Object, Object> map = new HashMap<>();
		map.put("recipe_user_id", recipe_user_id);
		map.put("recipe_idx", recipe_idx);

		try {
			if (feedService.cancelLikeRecipe(map) == 1) {
			
				return new ResponseEntity<String>("Success", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Fail", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Fail", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 레시피 수정
	@PutMapping("/update")
	public ResponseEntity<String> updateRecipe(UpdateRecipeFromClient recipe) {
		// 0.레시피 제목 수정
		HashMap<String, Object> map = new HashMap<>();
		map.put("recipe_idx", recipe.getRecipe_idx());
		map.put("title", recipe.getTitle());
		map.put("achieve_master", recipe.getAchieve_master());
		map.put("achieve_slave", recipe.getAchieve_slave());
		
		try {
			if (feedService.updateRecipeInfo(map) == 1) {
				
			} else {
			
				return new ResponseEntity<String>("Fail", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// 1.recipe_content 삭제
		List<Integer> deleteContents = recipe.getDeleteContents();
		if (deleteContents.get(0) != -1) {
			for (int i = 0; i < deleteContents.size(); i++) {
				try {
					if (feedService.deleteRecipeContent(deleteContents.get(i)) == 1) {
						
					} else {
					
						return new ResponseEntity<String>("Fail", HttpStatus.BAD_REQUEST);
					}
				} catch (Exception e) {
					e.printStackTrace();
					return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		}
		// 2.recipe_content 수정, 댓글:content_idx로 연결되어있어서 알아서 반영됨
		// recipe_content 삽입
		int recipe_idx = recipe.getRecipe_idx();
		List<Integer> content_idxList = recipe.getContent_idx();
		List<String> imgList = recipe.getImg();
		List<String> desList = recipe.getDes();
		List<String> thumbnailList = recipe.getThumbnail();
		List<Integer> content_orderList = recipe.getContent_order();

		for (int i = 0; i < content_idxList.size(); i++) {
			int content_idx = content_idxList.get(i);
			if (content_idx == -1) {
				// 삽입
				SaveRecipeContent content = new SaveRecipeContent();
				content.setRecipe_idx(recipe_idx);
				content.setImg(imgList.get(i));
				content.setDes(desList.get(i));
				content.setThumbnail(thumbnailList.get(i));
				content.setContent_order(content_orderList.get(i));

				try {
					if (feedService.writeRecipeContent(content) == 1) {
						
					} else {
						
						return new ResponseEntity<String>("Fail", HttpStatus.BAD_REQUEST);
					}
				} catch (Exception e) {
					e.printStackTrace();
					return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
				}

			} else {
				// 수정
				UpdateRecipeContentToDB recipeContent = new UpdateRecipeContentToDB();
				recipeContent.setIdx(content_idx);
				recipeContent.setImg(imgList.get(i));
				recipeContent.setDes(desList.get(i));
				recipeContent.setThumbnail(thumbnailList.get(i));
				recipeContent.setContent_order(content_orderList.get(i));

				try {
					if (feedService.updateRecipeContent(recipeContent) == 1) {
						
					} else {
						
						return new ResponseEntity<String>("Fail", HttpStatus.BAD_REQUEST);
					}
				} catch (Exception e) {
					e.printStackTrace();
					return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		}
		//3.해시태그 삭제
		List<Integer> deletehashtag = recipe.getDeletehashtag();
		if(deletehashtag.get(0) !=-1) {
			for(int i=0; i<deletehashtag.size(); i++) {
				int hashtag_idx = deletehashtag.get(i);
				try {
					if(feedService.deleteHashtag(hashtag_idx)==1) {
					
					}else {
						
						return new ResponseEntity<String>("Fail", HttpStatus.BAD_REQUEST);
					}
				} catch (Exception e) {
					e.printStackTrace();
					return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		}
		//4.해시태그 수정, 삽입
		map = new HashMap<>();
		List<Integer> hashtag_idx = recipe.getHashtag_idx();
		List<String> tag = recipe.getTag();
		for(int i=0; i<hashtag_idx.size(); i++) {
			int idx = hashtag_idx.get(i);
			//해시태그 삽입
			if(idx == -1) {
				map.put("recipe_idx", recipe_idx);
				map.put("hashtag", tag.get(i));
				try {
					if(feedService.writeHashtags(map)==1) {
						
					}else {
						
						return new ResponseEntity<String>("Fail", HttpStatus.BAD_REQUEST);
					}
				} catch (Exception e) {
					e.printStackTrace();
					return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}else {
				map.put("hashtag_idx", idx);
				map.put("tag", tag.get(i));
				
				try {
					if(feedService.updateHashtag(map)==1) {
						
					}else {
						
						return new ResponseEntity<String>("Fail", HttpStatus.BAD_REQUEST);
					}
				} catch (Exception e) {
					e.printStackTrace();
					return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		}
		
		return new ResponseEntity<String>("Success", HttpStatus.OK);
	}
	
	//writeRecipe-업적 확인 기능 추가
	@GetMapping("/write")
	public ResponseEntity<Map<String, Object>> RecipeAchieveList(){
		Map<String, Object> resultMap = new HashMap<>();
		List<AchieveListFromDB> achieveList;
		
		try {
			achieveList = feedService.getAchieveListForRecipe();
			if(achieveList == null) {
				resultMap.put("message", "FAIL");
				return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NO_CONTENT);
			}
			resultMap.put("achieveList", achieveList);
			resultMap.put("message", "Success");
			return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("message", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/popularposts")
	public ResponseEntity<Map<String, Object>> popularPosts(@RequestParam Map map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.OK;
		List<RecipeContent> list;
		try {
			int listCnt = feedService.getFeedCnt();
			int page = Integer.parseInt((String)map.get("page"));
			Paging paging = new Paging();
			paging.pageInfo(page, (page-1) * paging.getRangeSize(), listCnt);
			map.put("paging", paging);
			list = feedService.popularPosts(map);
			for(RecipeContent rc: list) {
				
				rc.setComment(feedService.getTotalCommentNum(rc.getIdx()));
				
				rc.setHashtags(feedService.getHashtagList(rc.getIdx()));
			}
			resultMap.put("popularPosts", list);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		resultMap.put("status", status);
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@GetMapping("/hashtagsearch")
	public ResponseEntity<Map<String, Object>> hashtagsearch(@RequestParam Map map) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String result = "SUCCESS";
		try {
			int listCnt = feedService.getFeedCnt();
			int page = Integer.parseInt((String)map.get("page"));
			Paging paging = new Paging();
			paging.pageInfo(page, (page-1) * paging.getRangeSize(), listCnt);
			map.put("paging", paging);
			List<RecipeContent> recipe = feedService.gethashtagRecipes(map);
			
			for(RecipeContent rc: recipe) {
				rc.setComment(feedService.getTotalCommentNum(rc.getIdx()));
				rc.setHashtags(feedService.getHashtagList(rc.getIdx()));
			}
			resultMap.put("hashtagfeed", recipe);

			if (recipe == null) {
				result = "FAIL";
			} else {
				result = "SUCCESS";
			}

			resultMap.put("message", result);
			status = HttpStatus.ACCEPTED;

		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

}
