<template>
	<div>
		<div class="crumbs">
			<el-breadcrumb separator="/">
				<el-breadcrumb-item>
					<i class="el-icon-lx-cascades"></i> 地点
				</el-breadcrumb-item>
			</el-breadcrumb>
		</div>
		<div class="container">
			<div class="handle-box">
				<el-button type="primary" icon="el-icon-delete" class="handle-del mr10" @click="delAllSelection">批量删除</el-button>
				<el-button type="primary" icon="el-icon-plus" class="handle-del mr10" @click="showModel('add')">增加</el-button>
				<!-- <el-select v-model="query.address" placeholder="地址" class="handle-select mr10">
					<el-option key="1" label="广东省" value="广东省"></el-option>
					<el-option key="2" label="湖南省" value="湖南省"></el-option>
				</el-select> -->
				<el-input v-model="form.name" placeholder="地点" class="handle-input mr10"></el-input>
				<el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
			</div>
			<el-table :data="tableData" border class="table" ref="multipleTable" header-cell-class-name="table-header"
			 @selection-change="handleSelectionChange">
				<el-table-column type="selection" width="55" align="center"></el-table-column>
				<el-table-column prop="id" label="ID" width="55" align="center"></el-table-column>
				<el-table-column prop="name" label="地点名"></el-table-column>
				<el-table-column label="日工资">
					<template slot-scope="scope">￥{{scope.row.money}}</template>
				</el-table-column>
				<!-- <el-table-column label="头像(查看大图)" align="center">
					<template slot-scope="scope">
						<el-image class="table-td-thumb" :src="scope.row.thumb" :preview-src-list="[scope.row.thumb]"></el-image>
					</template>
				</el-table-column> -->
				<!-- <el-table-column prop="address" label="地址"></el-table-column> -->
				<!-- <el-table-column label="状态" align="center">
					<template slot-scope="scope">
						<el-tag :type="scope.row.state==='成功'?'success':(scope.row.state==='失败'?'danger':'')">{{scope.row.state}}</el-tag>
					</template>
				</el-table-column> -->

				<el-table-column prop="time" label="注册时间"></el-table-column>
				<el-table-column label="操作" width="180" align="center">
					<template slot-scope="scope">
						<el-button type="text" icon="el-icon-edit" @click="showModel(scope.$index, scope.row)">编辑</el-button>
						<el-button type="text" icon="el-icon-delete" class="red" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="pagination">
				<el-pagination background layout="total, prev, pager, next" :current-page="query.pageIndex" :page-size="query.pageSize"
				 :total="pageTotal" @current-change="handlePageChange"></el-pagination>
			</div>
		</div>

		<!-- 编辑弹出框 -->
		<el-dialog title="增加" :visible.sync="editVisible" width="30%">
			<el-form ref="form" :model="form" label-width="70px">
				<el-form-item label="地点名">
					<el-input v-model="form.name"></el-input>
				</el-form-item>
				<el-form-item label="日工资">
					<el-input v-model="form.money"></el-input>
				</el-form-item>
			</el-form>
			<span slot="footer" class="dialog-footer">
				<el-button @click="editVisible = false">取 消</el-button>
				<el-button type="primary" @click="saveEdit">确 定</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
	// import {
	// 	fetchData
	// } from '../../api/index';
	
	
	export default {
		name: 'post',
		data() {
			return {
				query: {
					address: '',
					name: '',
					pageIndex: 1,
					pageSize: 10
				},
				tableData: [],
				isEdit: false,
				multipleSelection: [],
				delList: [],
				editVisible: false,
				pageTotal: 0,
				form: {},
				idx: -1,
				id: -1
			};
		},
		created() {
			this.getData();
		},
		methods: {
			// 获取 easy-mock 的模拟数据
			getData() {
				// fetchData(this.query).then(res => {
				//     console.log(res);
				//     this.tableData = res.list;
				//     this.pageTotal = res.pageTotal || 50;
				// });

				this.$axios
					.post(
						'/post/selectAll?currentPage=' +
						this.query.pageIndex +
						'&pageSize=' +
						this.query.pageSize,this.form
					)
					.then(res => {
						console.log(res);
						this.tableData = res.data.data.items;
						this.pageTotal = res.data.data.totalNum || 50;
					})
					.catch(err => {
						console.log(err)
					})



			},
			// 触发搜索按钮
			handleSearch() {
				// this.$set(this.query, 'pageIndex', 1);
				this.getData();
			},
			// 删除操作
			handleDelete(index, row) {
				const that = this;
				// 二次确认删除
				this.$confirm('确定要删除吗？', '提示', {
						type: 'warning'
					})
					.then(() => {
						this.$axios
							.post('/post/delete', [row.id])
							.then(res => {
								if (res.data) {
									this.$message.success('删除成功');
									this.tableData.splice(index, 1);
									this.getData()
								}
							})
							.catch(err => {
								console.log(err)
							})
					})
					.catch(() => {});
			},
			// 多选操作
			handleSelectionChange(val) {
				this.multipleSelection = val;
			},
			delAllSelection() {
				const length = this.multipleSelection.length;
				for (let i = 0; i < length; i++) {
					this.delList = this.delList.concat(this.multipleSelection[i].id);
				}
				this.$axios
					.post('/post/delete', this.delList)
					.then(res => {
						if (res.data) {
							this.$message.success('删除成功');
							this.getData()
						}
					})
					.catch(err => {
						console.log(err)
					})
				this.multipleSelection = [];
				this.delList = [];
			},
			// 编辑操作
			showModel(type, row) {
				this.editVisible = true;
				if (type === 'add') {
					this.clearData()
					this.modelTtile = '增加'
				} else if (type === 'update') {
					this.modelTtile = '修改信息'
					this.isEdit = true
					
					for (var key in this.form) {
						this.form[key] = row[key]
					}
					this.form = row
					this.form.id = row.id
				}
			},
			// showModel(index, row) {
			// 	this.idx = index;
			// 	this.form = row;
			// 	this.editVisible = true;
			// },
			// 保存编辑
			saveEdit() {
				if (this.isEdit) {
					this.$axios
						.post('/post/update', this.form)
						.then(res => {
							if (res.data) {
								this.$message.success(`修改第 ${this.idx + 1} 行成功`);
								this.$set(this.tableData, this.idx, this.form);
								this.getData()
								this.clearData()
								this.isEdit = false
								this.editVisible = false
							}
						})
						.catch(err => {
							console.log(err)
						})
				} else {
					this.$axios
						.post('/post/insert', this.form)
						.then(res => {
							if (res.data) {
								this.$message.info('添加成功')
								this.getData()
								this.clearData()
								this.editVisible = false
							}
						})
						.catch(err => {
							console.log(err)
						})
				}
			},
			// 分页导航
			handlePageChange(val) {
				this.$set(this.query, 'pageIndex', val);
				this.getData();
			},
			clearData() {
				this.form = {}
			},
		}
	};
</script>

<style scoped>
	.handle-box {
		margin-bottom: 20px;
	}

	.handle-select {
		width: 120px;
	}

	.handle-input {
		width: 300px;
		display: inline-block;
	}

	.table {
		width: 100%;
		font-size: 14px;
	}

	.red {
		color: #ff0000;
	}

	.mr10 {
		margin-right: 10px;
	}

	.table-td-thumb {
		display: block;
		margin: auto;
		width: 40px;
		height: 40px;
	}
</style>
